package eu.ncouret.wows.namecheckers.model.response.clans

import eu.ncouret.wows.namecheckers.model.ApiData

data class ClanSearchResponse(
    val clanId: Int,
    val createdAt: Int?,
    val membersCount: Int,
    val name: String,
    val tag: String
) : ApiData