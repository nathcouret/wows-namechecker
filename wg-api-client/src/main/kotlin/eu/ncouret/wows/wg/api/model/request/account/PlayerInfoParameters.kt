package eu.ncouret.wows.wg.api.model.request.account

import eu.ncouret.wows.wg.api.model.Language

data class PlayerInfoParameters(
    val accountId: String,
    val fields: List<String>? = listOf(),
    val language: Language? = Language.EN
)