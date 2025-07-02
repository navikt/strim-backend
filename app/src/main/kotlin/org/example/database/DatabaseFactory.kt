package org.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DatabaseFactory {
    fun init(): DataSource {
        return hikari()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://35.228.170.11:5432/strim"
            username = "strim-backend"
            password = "YOUR_ACTUAL_PASSWORD"
            driverClassName = "org.postgresql.Driver"
        }

        return HikariDataSource(config)
    }
}
