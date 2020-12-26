package eu.ncouret.wows.wg.api.model


data class MapApiResponse<out T : ApiData>(val meta: ApiMetadata, val status: String, val data: Map<String, T>)