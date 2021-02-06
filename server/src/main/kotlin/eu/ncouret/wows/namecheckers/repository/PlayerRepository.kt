package eu.ncouret.wows.namecheckers.repository

import eu.ncouret.wows.wg.api.model.Player
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PlayerRepository : ReactiveCrudRepository<Player, Long> {

    suspend fun findAllByNameLike(name: String): List<Player>

    suspend fun findAllByAccountIdOrderByUpdatedAtDesc(accountId: Long): List<Player>

    @Query("select p2.name, p1.account_id, p1.updated_at, p2.first_seen, p2.id\n" +
            "from (select account_id, max(updated_at) as updated_at\n" +
            "      from player\n" +
            "      group by account_id) as p1\n" +
            "         join player p2 on p2.account_id = p1.account_id and p1.updated_at = p2.updated_at\n" +
            "where p1.updated_at not between :updateAt and now()")
    suspend fun findUpdatedBefore(updatedAt: LocalDateTime): List<Player>
}