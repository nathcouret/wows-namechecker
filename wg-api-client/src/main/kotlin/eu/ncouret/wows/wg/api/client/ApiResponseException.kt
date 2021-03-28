package eu.ncouret.wows.wg.api.client

class ApiResponseException(override val message: String?) : Exception(message) {
}