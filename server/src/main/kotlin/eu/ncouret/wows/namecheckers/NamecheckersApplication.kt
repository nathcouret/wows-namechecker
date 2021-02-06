package eu.ncouret.wows.namecheckers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class NamecheckersApplication

fun main(args: Array<String>) {
    runApplication<NamecheckersApplication>(*args)
}
