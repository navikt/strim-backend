package strim

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
 class StrimApp

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger(StrimApp::class.java)
    logger.info("Hello, World!")
    runApplication<StrimApp>(*args)
}