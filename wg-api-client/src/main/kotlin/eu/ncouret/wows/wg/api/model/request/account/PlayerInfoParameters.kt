package eu.ncouret.wows.wg.api.model.request.account

import eu.ncouret.wows.wg.api.model.ApiRequest

data class PlayerInfoParameters(
        val accountId: String,
) : ApiRequest()