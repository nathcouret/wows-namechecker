package eu.ncouret.wows.namecheckers.model.request.account

import eu.ncouret.wows.namecheckers.model.Language

data class PlayerInfoParameters(
    val accountId: String,
    val fields: List<String>? = listOf(),
    val language: Language? = Language.EN
)