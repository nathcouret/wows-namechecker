package eu.ncouret.wows.namecheckers.business

import eu.ncouret.wows.namecheckers.repository.PlayerRepository
import eu.ncouret.wows.wg.api.client.WargamingApiClient
import eu.ncouret.wows.wg.api.client.fetchDictionary
import eu.ncouret.wows.wg.api.client.fetchList
import eu.ncouret.wows.wg.api.model.Player
import eu.ncouret.wows.wg.api.model.response.account.PlayerInfoResponse
import eu.ncouret.wows.wg.api.model.response.account.PlayerSearchResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class PlayersBusiness(
        val wgClient: WargamingApiClient,
        val playerRepository: PlayerRepository
) {

    private val logger = LoggerFactory.getLogger(PlayersBusiness::class.java)

    suspend fun searchPlayer(name: String, exact: Boolean = false): List<PlayerSearchResponse> {
        val params: MutableMap<String, String> = mutableMapOf(Pair("search", name))
        params["type"] = if (exact) "exact" else "startswith"
        return wgClient.fetchList<PlayerSearchResponse>("/account/list/", params).data
    }

    suspend fun searchPlayer(id: Long): Map<String, PlayerInfoResponse> {
        val params = mutableMapOf(Pair("account_id", id))
        return wgClient.fetchDictionary<PlayerInfoResponse>("/account/info", params).data
    }

    suspend fun searchPlayerInfo(ids: List<Long>): Map<String, PlayerInfoResponse> {
        val params = mutableMapOf(Pair("account_id", ids))
        return wgClient.fetchDictionary<PlayerInfoResponse>("/account/info/", params).data
    }

    @Transactional
    suspend fun searchName(name: String): Player {
        val localData = playerRepository.findAllByNameLike(name)
        if (localData.isEmpty()) {
            logger.debug("Player $name not found in local repository")
            val remoteData = searchPlayer(name, true)
            if (remoteData.isEmpty()) {
                logger.error("Player $name not found in remote repository")
                throw IllegalArgumentException("Not Found")
            }
            logger.debug("Found ${remoteData.size} entries in remote repository")
            return convertResponseToBusiness(remoteData.first())
                    .also { savePlayer(it) }
        }
        logger.debug("Found ${localData.size} entries in local repository")
        return localData.first()
    }

    suspend fun findPlayerHistory(accountId: Long): List<Player> {
        return playerRepository.findAllByAccountIdOrderByUpdatedAtDesc(accountId)
    }

    suspend fun getRemoteInfo(ids: List<Long>) = searchPlayerInfo(ids).mapValues {
        convertResponseToBusiness(it.value)
    }

    suspend fun getPlayersLastUpdatedBefore(time: LocalDateTime): List<Player> {
        return playerRepository.findUpdatedBefore(time)
    }

    suspend fun savePlayers(players: List<Player>) {
        playerRepository.saveAll(players).subscribe()
    }

    suspend fun savePlayer(player: Player) {
        logger.debug("saving player $player")
        playerRepository.save(Player(player.accountId, player.name)).subscribe()
    }

    private fun convertResponseToBusiness(response: PlayerSearchResponse): Player {
        return Player(response.accountId!!.toLong(), response.nickname)
    }

    private fun convertResponseToBusiness(response: PlayerInfoResponse): Player {
        return Player(response.accountId.toLong(), response.nickname)
    }

}