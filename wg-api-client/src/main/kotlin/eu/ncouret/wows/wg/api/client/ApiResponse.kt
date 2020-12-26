package eu.ncouret.wows.wg.api.client

import com.fasterxml.jackson.databind.JsonNode
import eu.ncouret.wows.wg.api.model.ApiMetadata

data class ApiResponse(
        val status: String,
        val meta: ApiMetadata?,
        val data: JsonNode?,
        val error: Error?,
)

data class Error(
        val field: String?,
        val code: Int,
        val message: String,
        val value: String?,
)