package eu.ncouret.wows.namecheckers.repository

import eu.ncouret.wows.wg.api.model.Player
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.Disposable

@Repository
class WritePlayerRepository(private val r2dbcEntityTemplate: R2dbcEntityTemplate) {

    private val logger = LoggerFactory.getLogger(WritePlayerRepository::class.java)

    fun add(player: Player): Disposable {
        logger.debug("inserting entity $player")
        return r2dbcEntityTemplate.insert(player).subscribe()
    }

    fun update(player: Player) = r2dbcEntityTemplate.update(player).subscribe()
}