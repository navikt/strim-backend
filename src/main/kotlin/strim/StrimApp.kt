package strim

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
 class StrimApp

fun main(args: Array<String>) {
    checkEnv()
    runApplication<StrimApp>(*args)
}

@PostConstruct
fun checkEnv() {
    println("DB_HOST=${System.getenv("DB_HOST")}")
    println("DB_PASSWORD=${System.getenv("DB_PASSWORD")}")
}
