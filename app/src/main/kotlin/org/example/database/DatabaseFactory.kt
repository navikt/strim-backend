package org.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://35.228.170.11:5432/strim"
            driverClassName = "org.postgresql.Driver"
            username = "strim-backend"
            password = "I8BQJq)sKvo_PyeK"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }


        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}
