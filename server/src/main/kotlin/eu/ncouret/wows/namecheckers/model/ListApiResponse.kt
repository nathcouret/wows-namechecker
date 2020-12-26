package eu.ncouret.wows.namecheckers.model

import eu.ncouret.wows.namecheckers.model.ApiMetadata


data class ListApiResponse<out T : ApiData>(val meta: ApiMetadata, val status: String, val data: List<T>)