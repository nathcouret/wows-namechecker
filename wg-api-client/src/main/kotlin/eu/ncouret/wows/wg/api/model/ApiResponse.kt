package eu.ncouret.wows.wg.api.model

import com.fasterxml.jackson.databind.JsonNode

/**
 * Represents a response from the WG API. The data and meta field are set if the request is successful, otherwise the error field is set.
 * Success is determined by the status field.
 */
data class ApiResponse(
        val status: String,
        val meta: ApiMetadata?,
        val data: JsonNode?,
        val error: ApiError?,
) {
    /**
     * Construct a new error response.
     */
    constructor(status: String, error: ApiError) : this(status, null, null, error)
}

/**
 * Represents the detail of an error in the api response.
 */
data class ApiError(
        val field: String?,
        val code: Int,
        val message: String,
        val value: String?,
) {
    /**
     * Construct a new instance with just a code and message field.
     */
    constructor(code: Int, message: String) : this(null, code, message, null)
}

/**
 * Represents the metadata of a successful response.
 */
data class ApiMetadata(
        val count: Int,
        val pageTotal: Int? = null,
        val total: Int? = null,
        val limit: Int? = null,
        val page: Int? = null
)