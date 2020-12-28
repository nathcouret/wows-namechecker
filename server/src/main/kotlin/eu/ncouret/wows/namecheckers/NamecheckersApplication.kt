package eu.ncouret.wows.namecheckers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NamecheckersApplication

fun main(args: Array<String>) {
    runApplication<NamecheckersApplication>(*args)
}
