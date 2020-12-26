package eu.ncouret.wows.namecheckers.business

import eu.ncouret.wows.namecheckers.model.Player
import eu.ncouret.wows.namecheckers.model.response.account.PlayerSearchResponse
import eu.ncouret.wows.namecheckers.repository.ReadPlayerRepository
import eu.ncouret.wows.namecheckers.repository.WritePlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PlayersBusiness(val readPlayerRepository: ReadPlayerRepository,
                      val wgClient: WargamingApiClient,
                      val writePlayerRepository: WritePlayerRepository
) {

    private val logger = LoggerFactory.getLogger(PlayersBusiness::class.java)

    suspend fun searchPlayer(name: String, exact: Boolean = false): List<PlayerSearchResponse> {
        val params: MutableMap<String, String> = mutableMapOf(Pair("search", name))
        params["type"] = if (exact) "exact" else "startswith"
        return wgClient.fetchList<PlayerSearchResponse>("/account/list/", params).data
    }

    suspend fun searchName(name: String): Player {
        val localData = readPlayerRepository.findAllByName(name)
        if (localData.isEmpty()) {
            logger.debug("Player $name not found in local repository")
            val remoteData = searchPlayer(name, true)
            if (remoteData.isEmpty()) {
                logger.error("Player $name not found in remote repository")
                throw IllegalArgumentException("Not Found")
            }
            logger.debug("Found ${remoteData.size} entries in remote repository")
            return remoteData.first()
                    .let { Player(it.accountId?.toLong()!!, it.nickname, LocalDateTime.now()) }
                    .also { player ->
                        withContext(Dispatchers.IO){
                            writePlayerRepository.add(player) }
                    }
        }
        logger.debug("Found ${localData.size} entries in local repository")
        return localData.first()
    }

}