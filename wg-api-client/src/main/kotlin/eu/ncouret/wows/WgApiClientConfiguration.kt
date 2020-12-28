package eu.ncouret.wows

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import eu.ncouret.wows.wg.api.client.ResponseMapper
import eu.ncouret.wows.wg.api.client.WargamingApiClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(ApiProperties::class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
class WgApiClientConfiguration {

    @Bean
    fun wgWebClient(apiProperties: ApiProperties): WebClient = WebClient.builder()
            .baseUrl(apiProperties.url)
            .defaultUriVariables(mapOf(Pair("application_id", apiProperties.key)))
            .build()

    @Bean
    fun wgJacksonBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        builder.failOnUnknownProperties(false)
        builder.failOnEmptyBeans(false)
        return builder
    }

    @Bean
    fun objectMapper(@Qualifier("wgJacksonBuilder") builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder.build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun responseMapper(mapper: ObjectMapper): ResponseMapper = ResponseMapper(mapper)

    @Bean
    @ConditionalOnMissingBean
    fun wargamingApiClient(apiProperties: ApiProperties, @Qualifier("wgWebClient") webClient: WebClient, responseMapper: ResponseMapper): WargamingApiClient =
            WargamingApiClient(apiProperties, webClient, responseMapper)
}