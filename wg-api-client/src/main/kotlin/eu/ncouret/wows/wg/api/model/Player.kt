package eu.ncouret.wows.wg.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
data class Player(@Column @Id val id: Long,
                  @Column val name: String,
                  @Column val firstSeen: LocalDateTime)