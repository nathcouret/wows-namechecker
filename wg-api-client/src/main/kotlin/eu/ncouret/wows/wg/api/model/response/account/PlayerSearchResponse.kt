package eu.ncouret.wows.wg.api.model.response.account

import eu.ncouret.wows.wg.api.model.ApiData

data class PlayerSearchResponse(val nickname: String, val accountId: String?): ApiData