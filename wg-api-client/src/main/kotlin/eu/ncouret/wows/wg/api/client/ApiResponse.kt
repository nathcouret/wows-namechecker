package eu.ncouret.wows.wg.api.client

import com.fasterxml.jackson.databind.JsonNode
import eu.ncouret.wows.wg.api.model.ApiMetadata

data class ApiResponse(
        val status: String,
        val meta: ApiMetadata?,
        val data: JsonNode?,
        val error: ApiError?,
) {
    constructor(status: String, error: ApiError?) : this(status, null, null, error)
}

data class ApiError(
        val field: String?,
        val code: Int,
        val message: String,
        val value: String?,
) {
    constructor(code: Int, message: String) : this(null, code, message, null)
}