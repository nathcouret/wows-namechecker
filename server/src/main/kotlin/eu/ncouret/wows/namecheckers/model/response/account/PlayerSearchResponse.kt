package eu.ncouret.wows.namecheckers.model.response.account

import eu.ncouret.wows.namecheckers.model.ApiData

data class PlayerSearchResponse(val nickname: String, val accountId: String?): ApiData