package strim

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    @Profile("!local")
    fun jwtDecoder(
        @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") jwkSetUri: String
    ): JwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()

    @Bean
    @Profile("local")
    fun localFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
        return http.build()
    }

    @Bean
    @Profile("!local")
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/internal/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/events/*/join").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/events/*/join").authenticated()
                    .requestMatchers(HttpMethod.GET, "/me").permitAll()
                    .anyRequest().denyAll()
            }
            .oauth2ResourceServer { rs ->
                rs.jwt { }
            }

        return http.build()
    }
}
