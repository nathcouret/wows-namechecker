package eu.ncouret.wows.wg.api.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.ncouret.wows.wg.api.model.ApiData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

data class Dummy(
        val name: String,
        val number: Int
) : ApiData

class ResponseMapperTest {

    private lateinit var mapper: ResponseMapper
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        mapper = ResponseMapper(objectMapper)
    }

    @Test
    fun listResponseData() {
        // Given
        val node: JsonNode = objectMapper.valueToTree(listOf(Dummy("toto", 1)))
        // When
        val response: List<Dummy> = mapper.listResponseData(node, Dummy::class.java)
        // Then
        assertEquals(1, response.size)
        assertEquals("toto", response[1].name)
        assertEquals(1, response[1].number)
    }

    @Test
    fun mapResponseData() {
        // Given
        val node: JsonNode = objectMapper.valueToTree(mapOf(Pair("toto", Dummy("toto", 1))))
        // When
        val response: Map<String, Dummy> = mapper.mapResponseData(node, Dummy::class.java)
        // Then
        assertEquals(1, response.size)
        assertNotNull(response["toto"])
        assertEquals("toto", response["toto"]?.name)
        assertEquals(1, response["toto"]?.number)
    }
}