package eu.ncouret.wows.wg.api.model.request.account

import eu.ncouret.wows.wg.api.model.ApiData

data class PlayerSearchParameters(val search: String, val type: SearchType? = SearchType.STARTS_WITH): ApiData