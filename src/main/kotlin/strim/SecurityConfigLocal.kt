package strim

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("local")
class SecurityConfigLocal {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/internal/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                    .requestMatchers(HttpMethod.GET, "/me").permitAll()
                    .requestMatchers(HttpMethod.POST, "/events/create").permitAll()
                    .requestMatchers(HttpMethod.POST, "/events/*/join").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/events/*/join").permitAll()
                    .anyRequest().permitAll()
            }

        return http.build()
    }
}
