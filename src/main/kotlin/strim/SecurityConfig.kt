package strim

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("!local")
class SecurityConfigProd {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/internal/**").permitAll()

                it.requestMatchers(HttpMethod.GET, "/events/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/categories").permitAll()

                it.requestMatchers(HttpMethod.POST, "/events/*/join").authenticated()
                it.requestMatchers(HttpMethod.DELETE, "/events/*/join").authenticated()

                it.requestMatchers(HttpMethod.POST, "/events/create").authenticated()

                it.requestMatchers(HttpMethod.PATCH, "/events/*").authenticated()

                it.anyRequest().denyAll()

            }
            .oauth2ResourceServer { it.jwt { } }

        return http.build()
    }
}
