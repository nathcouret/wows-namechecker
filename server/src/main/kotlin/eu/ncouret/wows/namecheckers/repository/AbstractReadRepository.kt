package eu.ncouret.wows.namecheckers.repository

import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual

abstract class AbstractReadRepository<out T>(
        protected val template: R2dbcEntityTemplate,
        private val entity: Class<out T>
) {

    suspend fun findBy(vararg columns: Pair<String, Any>): List<T> {
        val select = template.select(entity)
        val criterion = mutableListOf<Criteria>()
        for (column in columns) {
            criterion.add(Criteria.where(column.first).isEqual(column.second))
        }
        val andQuery = criterion.reduceOrNull(Criteria::and)
        return if (andQuery != null) {
            select.matching(Query.query(andQuery)).all().collectList().awaitFirst()
        } else {
            select.all().collectList().awaitFirst()
        }
    }
}