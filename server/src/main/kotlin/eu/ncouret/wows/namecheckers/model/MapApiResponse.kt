package eu.ncouret.wows.namecheckers.model

import eu.ncouret.wows.namecheckers.model.ApiMetadata


data class MapApiResponse<out T : ApiData>(val meta: ApiMetadata, val status: String, val data: Map<String, T>)