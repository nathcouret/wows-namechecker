package eu.ncouret.wows.namecheckers

import eu.ncouret.wows.WgApiClientConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(WgApiClientConfiguration::class)
class NamecheckersApplication

fun main(args: Array<String>) {
    runApplication<NamecheckersApplication>(*args)
}
