package eu.ncouret.wows.wg.api.model


data class ListApiResponse<out T : ApiData>(val meta: ApiMetadata, val status: String, val data: List<T>)