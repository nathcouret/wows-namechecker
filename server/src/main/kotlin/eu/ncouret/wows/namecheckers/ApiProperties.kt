package eu.ncouret.wows.namecheckers

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("api")
class ApiProperties {

    lateinit var key: String
    lateinit var url: String
}