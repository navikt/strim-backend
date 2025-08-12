package strim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
 class StrimApp

fun main(args: Array<String>) {
    runApplication<StrimApp>(*args)
}
