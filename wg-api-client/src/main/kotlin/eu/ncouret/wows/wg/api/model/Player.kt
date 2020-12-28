package eu.ncouret.wows.wg.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table
data class Player(
        @Column val accountId: Long,
        @Column val name: String,
        @Column val firstSeen: LocalDateTime,
        @Column val updatedAt: LocalDateTime,
        @Column @Id val id: UUID? = null
) {
    constructor(accountId: Long, name: String) : this(accountId, name, LocalDateTime.now(), LocalDateTime.now())
    constructor(accountId: Long, name: String, firstSeen: LocalDateTime) : this(accountId, name, firstSeen, LocalDateTime.now())
}