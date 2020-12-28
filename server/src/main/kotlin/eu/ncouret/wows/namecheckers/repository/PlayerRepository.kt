package eu.ncouret.wows.namecheckers.repository

import eu.ncouret.wows.wg.api.model.Player
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : ReactiveCrudRepository<Player, Long> {

    suspend fun findAllByNameStartingWith(name: String): List<Player>

    suspend fun findAllByNameLike(name: String): List<Player>

    suspend fun findAllByIdOrderByFirstSeenDesc(id: Long): List<Player>
}