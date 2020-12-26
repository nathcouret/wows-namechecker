package eu.ncouret.wows.namecheckers.business

import eu.ncouret.wows.namecheckers.ApiProperties
import eu.ncouret.wows.namecheckers.model.ApiData
import eu.ncouret.wows.namecheckers.model.EnumValue
import eu.ncouret.wows.namecheckers.model.ListApiResponse
import eu.ncouret.wows.namecheckers.model.MapApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class WargamingApiClient(
        val apiProperties: ApiProperties,
        val client: WebClient,
        val responseMapper: ResponseMapper
) {

    private val logger = LoggerFactory.getLogger(WargamingApiClient::class.java)

    /**
     * Send a query to the Wargaming API and retrieve the result as a string to be processed later
     */
    suspend fun sendQuery(
            url: String,
            parameters: Map<String, Any?>
    ): ApiResponse =
            client.get().uri { builder ->
                builder.path(url)
                builder.queryParam("application_id", apiProperties.key)
                normalizeParams(parameters).forEach { builder.queryParam(it.key, it.value) }
                val uri = builder.build()
                logger.debug("Querying $uri")
                uri
            }.accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .awaitBody()

    private fun normalizeParams(rawParams: Map<String, Any?>): Map<String, String> {
        val params = mutableMapOf<String, String>()
        rawParams.filterValues { it != null }.forEach { (key, value) ->
            params[key] = when (value) {
                is Map<*, *> -> value.asIterable().filterNot { it.key == null }
                        .joinToString(",") { (key, value) ->
                            "${normalizeParam(key)}=${normalizeParam(value)}"
                        }
                is List<*> -> value.filterNotNull()
                        .joinToString(",") { normalizeParam(it) }
                else -> normalizeParam(value)
            }
        }
        return params
    }

    private fun normalizeParam(param: Any?): String = when (param) {
        null -> ""
        is String -> param
        is EnumValue -> param.getValue()
        else -> param.toString()
    }
}

suspend inline fun <reified T : ApiData> WargamingApiClient.fetchList(url: String, params: Map<String, Any?> = mapOf()): ListApiResponse<T> {
    val response = sendQuery(url, params)
    if (response.status == "error") {
        throw IllegalArgumentException(response.error?.message)
    }
    val data = responseMapper.listResponseData(response.data, T::class.java)
    return ListApiResponse(response.meta!!, response.status, data)
}

suspend inline fun <reified T : ApiData> WargamingApiClient.fetchDictionary(url: String, params: Map<String, Any?> = mapOf()): MapApiResponse<T> {
    val response = sendQuery(url, params)
    if (response.status == "error") {
        throw IllegalArgumentException(response.error?.message)
    }
    val data = responseMapper.mapResponseData(response.data, T::class.java)
    return MapApiResponse(response.meta!!, response.status, data)
}