package eu.ncouret.wows.namecheckers.business

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.ncouret.wows.namecheckers.model.ApiData
import eu.ncouret.wows.namecheckers.model.ListApiResponse
import eu.ncouret.wows.namecheckers.model.MapApiResponse
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ResponseMapper(val mapper: ObjectMapper) {

    fun <T : ApiData> listResponse(response: String, clazz: KClass<T>): List<T> =
            mapper.readValue<ListApiResponse<T>>(
                    response,
                    mapper.typeFactory.constructParametricType(ListApiResponse::class.java, clazz.java)
            ).data

    fun <T : ApiData> listResponseData(data: JsonNode?, clazz: Class<T>): List<T> = mapper.convertValue(
            data,
            mapper.typeFactory.constructCollectionLikeType(ArrayList::class.java, clazz)
    )

    fun <T : ApiData> mapResponseData(data: JsonNode?, clazz: Class<T>): Map<String, T> = mapper.convertValue(
            data,
            mapper.typeFactory.constructMapLikeType(HashMap::class.java, String::class.java, clazz)
    )

    fun <T : ApiData> mapResponse(response: String, clazz: KClass<T>): List<T> = mapper.readValue<MapApiResponse<T>>(
            response,
            mapper.typeFactory.constructParametricType(MapApiResponse::class.java, clazz.java)
    ).data.values.toList()
}