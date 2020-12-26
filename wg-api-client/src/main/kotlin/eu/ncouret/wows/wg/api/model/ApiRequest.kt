package eu.ncouret.wows.wg.api.model

open class ApiRequest(val fields: List<String> = listOf(), val language: Language = Language.EN)