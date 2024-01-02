package ru.nifontbus.testjwt.jwt

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
	private val jwtFilter: JwtFilter
) {

	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		return http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests {
				it
					.requestMatchers("/api/auth/login", "/api/auth/token").permitAll()
					.anyRequest().authenticated()
					.and()
					.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
			}.build()
	}
}