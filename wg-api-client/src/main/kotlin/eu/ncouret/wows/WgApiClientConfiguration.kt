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
@EnableConfigurationProperties(WgApiProperties::class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
class WgApiClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun wgWebClient(wgApiProperties: WgApiProperties): WebClient = WebClient.builder()
            .baseUrl(wgApiProperties.url)
            .defaultUriVariables(mapOf(Pair("application_id", wgApiProperties.key)))
            .build()

    @Bean
    @ConditionalOnMissingBean
    fun wgJacksonBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        builder.failOnUnknownProperties(false)
        builder.failOnEmptyBeans(false)
        return builder
    }

    @Bean
    @ConditionalOnMissingBean
    fun wgObjectMapper(@Qualifier("wgJacksonBuilder") builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder.build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun wgResponseMapper(@Qualifier("wgObjectMapper") mapper: ObjectMapper): ResponseMapper = ResponseMapper(mapper)

    @Bean
    @ConditionalOnMissingBean
    fun wargamingApiClient(
            wgApiProperties: WgApiProperties,
            @Qualifier("wgWebClient") webClient: WebClient,
            @Qualifier("wgResponseMapper") responseMapper: ResponseMapper
    ): WargamingApiClient = WargamingApiClient(wgApiProperties, webClient, responseMapper)
}