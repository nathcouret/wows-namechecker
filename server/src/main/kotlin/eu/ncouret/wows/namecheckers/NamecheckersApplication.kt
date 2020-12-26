package eu.ncouret.wows.namecheckers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
@EnableConfigurationProperties(ApiProperties::class)
class NamecheckersApplication {

    @Bean
    fun webClient(apiProperties: ApiProperties): WebClient = WebClient.builder()
            .baseUrl(apiProperties.url)
            .defaultUriVariables(mapOf(Pair("application_id", apiProperties.key)))
            .build()

    @Bean
    fun jacksonBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        builder.failOnUnknownProperties(false)
        builder.failOnEmptyBeans(false)
        return builder
    }

    @Bean
    fun objectMapper(@Qualifier("jacksonBuilder") builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder.build()
    }
}

fun main(args: Array<String>) {
    runApplication<NamecheckersApplication>(*args)
}
