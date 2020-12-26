package eu.ncouret.wows.namecheckers.business

import com.fasterxml.jackson.databind.JsonNode
import eu.ncouret.wows.namecheckers.model.ApiMetadata

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