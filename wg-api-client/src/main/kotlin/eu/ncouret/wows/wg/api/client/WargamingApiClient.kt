package eu.ncouret.wows.wg.api.client

import eu.ncouret.wows.WgApiProperties
import eu.ncouret.wows.wg.api.model.*
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.reactive.awaitFirst
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

/**
 * The main client to the Wargaming API.
 */
@Service
class WargamingApiClient(
        val wgApiProperties: WgApiProperties,
        val client: WebClient,
        val responseMapper: ResponseMapper
) {

    private val logger = LoggerFactory.getLogger(WargamingApiClient::class.java)

    /**
     * Send a query to the Wargaming API and retrieve the result as a string to be processed later
     * @param url The url to send the request to
     * @param parameters Extra request parameters
     * @return A response wrapper
     */
    suspend fun sendQuery(
            url: String,
            parameters: Map<String, Any?>
    ): ApiResponse = doSendQuery(url, parameters).awaitFirst()

    /**
     * Resilient call to the Wargaming API. A circuit breaker is applied to mitigate failures to call the service
     * and a ratelimiter keeps the call rate within API limits.
     * @param url The url to send the request to
     * @param parameters Extra request parameters
     * @return The response
     */
    @RateLimiter(name = "wg")
    @CircuitBreaker(name = "wg", fallbackMethod = "fallback")
    fun doSendQuery(
            url: String,
            parameters: Map<String, Any?>
    ): Mono<ApiResponse> = client.get().uri { builder ->
        builder.path(url)
        builder.queryParam("application_id", wgApiProperties.key)
        normalizeParameters(parameters).forEach { builder.queryParam(it.key, it.value) }
        val uri = builder.build()
        logger.debug("Querying $uri")
        uri
    }.accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ApiResponse::
            class.java)

    /**
     * Fallback function if the wg circuit breaker is open
     * @param url The url to send the request to
     * @param parameters Extra request parameters
     * @param error The exception that tripped the circuit breaker
     * @return An error response
     */
    @SuppressWarnings("unused")
    fun fallback(url: String, parameters: Map<String, Any?>, error: WebClientResponseException): Mono<ApiResponse> {
        val apiError = ApiError(error.statusCode.value(), error.localizedMessage)
        return Mono.just(ApiResponse("error", apiError))
    }

    /**
     * Convert a map of request parameters to values as string representation. Null values are filtered out if the flag is set
     * @param rawParameters The map whose values to convert
     * @param filterNull Flag indicating whether null values should be filtered out
     * @return The converted map of parameters
     */
    private fun normalizeParameters(rawParameters: Map<String, Any?>, filterNull: Boolean = false): Map<String, String> {
        return rawParameters.filterValues { it != null && filterNull }.mapValues { (_, value) ->
            when (value) {
                is Map<*, *> -> value.asIterable().filterNot { it.key == null }
                        .joinToString(",") { (key, value) ->
                            "${normalizeParameter(key)}=${normalizeParameter(value)}"
                        }
                is List<*> -> value.filterNotNull()
                        .joinToString(",") { normalizeParameter(it) }
                else -> normalizeParameter(value)
            }
        }
    }

    /**
     * Convert the given parameter to its String representation. If null, an empty string is returned
     * @param param The parameter to convert
     * @return The string representation of the param
     */
    private fun normalizeParameter(param: Any?): String = when (param) {
        null -> ""
        is String -> param
        is EnumValue -> param.getValue()
        else -> param.toString()
    }
}

/**
 * Send a request to the WG API that returns data as a list of T.
 *
 * @param url The url to send the request to
 * @param parameters Extra request parameters
 * @return The response with data as a List<T>
 */
suspend inline fun <reified T : ApiData> WargamingApiClient.fetchList(url: String, parameters: Map<String, Any?> = mapOf()): ListApiResponse<T> {
    val response = sendQuery(url, parameters)
    if (response.status == "error") {
        throw ApiResponseException(response.error?.message)
    }
    val data = responseMapper.listResponseData(response.data, T::class.java)
    return ListApiResponse(response.meta!!, response.status, data)
}

/**
 * Send a request to the WG API that returns data as a dictionary of string key and T values.
 * @param url The url to send the request to
 * @param parameters Extra request parameters
 * @return The response with data as a Map<String, T>
 */
suspend inline fun <reified T : ApiData> WargamingApiClient.fetchDictionary(url: String, parameters: Map<String, Any?> = mapOf()): MapApiResponse<T> {
    val response = sendQuery(url, parameters)
    if (response.status == "error") {
        throw ApiResponseException(response.error?.message)
    }
    val data: Map<String, T> = responseMapper.mapResponseData(response.data, T::class.java)
    return MapApiResponse(response.meta!!, response.status, data)
}