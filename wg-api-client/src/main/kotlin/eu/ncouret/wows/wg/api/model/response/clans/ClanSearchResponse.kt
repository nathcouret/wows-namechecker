package eu.ncouret.wows.wg.api.model.response.clans

import eu.ncouret.wows.wg.api.model.ApiData

data class ClanSearchResponse(
    val clanId: Int,
    val createdAt: Int?,
    val membersCount: Int,
    val name: String,
    val tag: String
) : ApiData