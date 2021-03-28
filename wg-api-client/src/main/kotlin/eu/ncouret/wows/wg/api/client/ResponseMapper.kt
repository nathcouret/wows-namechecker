package eu.ncouret.wows.wg.api.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.ncouret.wows.wg.api.model.ApiData

/**
 * Map API responses
 */
class ResponseMapper(private val mapper: ObjectMapper) {

    /**
     * Map a response's data to a list of objects of the given class
     * @param data The json to convert
     * @param clazz The class of object to convert to
     * @return A list of objects
     */
    fun <T : ApiData> listResponseData(data: JsonNode?, clazz: Class<T>): List<T> = mapper.convertValue(
            data,
            mapper.typeFactory.constructCollectionLikeType(ArrayList::class.java, clazz)
    )

    /**
     * Map a responses's data to a map of objects of the given class, with the keys being of String type
     * @param data The json to convert
     * @param clazz The class of values
     * @param keyClass The class of keys, defaults to String
     * @return A map of objects
     */
    fun <T : ApiData, U> mapResponseData(data: JsonNode?, clazz: Class<T>, keyClass: Class<*> = String::class.java): Map<U, T> =
            mapper.convertValue(
                    data,
                    mapper.typeFactory.constructMapLikeType(HashMap::class.java, keyClass, clazz)
            )
}