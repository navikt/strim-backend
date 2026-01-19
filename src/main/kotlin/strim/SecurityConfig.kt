package strim

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/internal/actuator/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/events/create").permitAll()
                    .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                    .requestMatchers("/internal/**").permitAll()
                    .anyRequest().denyAll()
            }
        return http.build()
    }
}
