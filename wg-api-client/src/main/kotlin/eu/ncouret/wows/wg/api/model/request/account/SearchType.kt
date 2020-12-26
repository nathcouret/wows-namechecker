package eu.ncouret.wows.wg.api.model.request.account

import eu.ncouret.wows.wg.api.model.EnumValue

enum class SearchType(private val type: String) : EnumValue {
    STARTS_WITH("startswith"),
    EXACT("exact");

    override fun getValue(): String {
        return type
    }
}
