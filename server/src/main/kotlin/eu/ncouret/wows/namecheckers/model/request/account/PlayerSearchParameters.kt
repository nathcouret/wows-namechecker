package eu.ncouret.wows.namecheckers.model.request.account

import eu.ncouret.wows.namecheckers.model.ApiData

data class PlayerSearchParameters(val search: String, val type: SearchType? = SearchType.STARTS_WITH): ApiData