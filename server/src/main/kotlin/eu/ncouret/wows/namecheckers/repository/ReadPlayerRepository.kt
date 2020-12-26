package eu.ncouret.wows.namecheckers.repository

import eu.ncouret.wows.namecheckers.model.Player
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository

@Repository
class ReadPlayerRepository(val r2dbcEntityTemplate: R2dbcEntityTemplate) : AbstractReadRepository<Player>(r2dbcEntityTemplate, Player::class.java) {

    suspend fun count(): Long = template.select(Player::class.java).count().awaitFirst()

    suspend fun findAllById(id: Long): List<Player> = findBy("id", id)

    suspend fun findAllByName(name: String): List<Player> = findBy("name", name)

    suspend fun findBy(column: String, value: Any): List<Player> = findBy(Pair(column, value))
}