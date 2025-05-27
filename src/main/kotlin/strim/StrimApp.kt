package strim

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private val logger: Logger = LoggerFactory.getLogger(StrimApp::class.java)
@SpringBootApplication
open class StrimApp

fun main(args: Array<String>) {
    runApplication<StrimApp>(*args) {
        logger.info("Hello world")
        setAdditionalProfiles(System.getenv("MILJO"))
    }
}