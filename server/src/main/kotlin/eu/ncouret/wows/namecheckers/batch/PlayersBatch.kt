package eu.ncouret.wows.namecheckers.batch

import eu.ncouret.wows.namecheckers.business.PlayersBusiness
import eu.ncouret.wows.wg.api.model.Player
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class PlayersBatch(
        val playersBusiness: PlayersBusiness,
        @Value("\${namecheckers.batch.delay}") val batchDelay: Duration
) {

    private val logger = LoggerFactory.getLogger(PlayersBatch::class.java)

    @Scheduled(cron = "\${namecheckers.batch.cron}")
    fun schedule() {
        logger.debug("Batch begins")
        val oneWeek = LocalDateTime.now().minus(batchDelay)
        runBlocking {
            val playersToSearch = playersBusiness.getPlayersLastUpdatedBefore(oneWeek)
            logger.debug("${playersToSearch.size} players to update")
            playersToSearch.chunked(100).forEach {
                val playersToInsert = comparePlayersWithRemote(it)
                logger.debug("${playersToInsert.size} players to insert")
                playersBusiness.savePlayers(playersToInsert)
            }
        }
        logger.debug("batch ends")
    }

    suspend fun comparePlayersWithRemote(players: List<Player>): List<Player> {
        if (players.size > 100) {
            throw IllegalArgumentException(100.toString())
        }
        val remotePlayers: Map<String, Player> = playersBusiness.getRemoteInfo(players.map { it.accountId })
        val playersToInsert: MutableList<Player> = mutableListOf()
        for (player in players) {
            val id = player.accountId.toString()
            if (remotePlayers.containsKey(id) && remotePlayers[id] != null) {
                val remotePlayer = remotePlayers[id]
                if (remotePlayer?.name != player.name) {
                    logger.debug("Found a new name ${player.name} -> ${remotePlayer?.name}")
                    playersToInsert.add(Player(player.accountId, remotePlayer!!.name))
                } else {
                    playersToInsert.add(Player(player.accountId, player.name, player.firstSeen, LocalDateTime.now(), player.id))
                }
            }
        }
        return playersToInsert
    }
}