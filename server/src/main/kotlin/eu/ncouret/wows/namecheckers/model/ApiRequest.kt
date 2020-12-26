package eu.ncouret.wows.namecheckers.model

import eu.ncouret.wows.namecheckers.model.Language

open class ApiRequest(val fields: List<String> = listOf(), val language: Language = Language.EN)