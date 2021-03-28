package eu.ncouret.wows

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("api")
class WgApiProperties {

    lateinit var key: String
    lateinit var url: String
}