package eu.ncouret.wows.wg.api.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.ncouret.wows.wg.api.model.ApiData
import org.springframework.stereotype.Component

@Component
class ResponseMapper(val mapper: ObjectMapper) {

    fun <T : ApiData> listResponseData(data: JsonNode?, clazz: Class<T>): List<T> = mapper.convertValue(
            data,
            mapper.typeFactory.constructCollectionLikeType(ArrayList::class.java, clazz)
    )

    fun <T : ApiData> mapResponseData(data: JsonNode?, clazz: Class<T>): Map<String, T> = mapper.convertValue(
            data,
            mapper.typeFactory.constructMapLikeType(HashMap::class.java, String::class.java, clazz)
    )
}