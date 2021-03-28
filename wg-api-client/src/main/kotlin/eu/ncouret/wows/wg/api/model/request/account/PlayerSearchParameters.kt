package eu.ncouret.wows.wg.api.model.request.account

import eu.ncouret.wows.wg.api.model.ApiRequest
import eu.ncouret.wows.wg.api.model.EnumValue

data class PlayerSearchParameters(val search: String, val type: SearchType? = SearchType.STARTS_WITH) : ApiRequest()

enum class SearchType(private val type: String) : EnumValue {
    STARTS_WITH("startswith"),
    EXACT("exact");

    override fun getValue(): String {
        return type
    }
}