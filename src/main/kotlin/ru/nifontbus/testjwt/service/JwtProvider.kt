package ru.nifontbus.testjwt.service

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nifontbus.testjwt.model.User
import java.security.Key
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtProvider(
	@Value("\${jwt.secret.access}") jwtAccessSecret: String,
	@Value("\${jwt.secret.refresh}") jwtRefreshSecret: String
) {
	private val jwtAccessSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret))
	private val jwtRefreshSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret))

	fun generateAccessToken(user: User): String {
		val now: LocalDateTime = LocalDateTime.now()
		val accessExpirationInstant: Instant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()
		val accessExpiration: Date = Date.from(accessExpirationInstant)
		return Jwts.builder()
			.setSubject(user.login)
			.setExpiration(accessExpiration)
			.signWith(jwtAccessSecret)
			.claim("roles", user.roles)
			.claim("firstName", user.firstName)
			.compact()
	}

	fun generateRefreshToken(user: User): String {
		val now: LocalDateTime = LocalDateTime.now()
		val refreshExpirationInstant: Instant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant()
		val refreshExpiration: Date = Date.from(refreshExpirationInstant)
		return Jwts.builder()
			.setSubject(user.login)
			.setExpiration(refreshExpiration)
			.signWith(jwtRefreshSecret)
			.compact()
	}

	fun validateAccessToken(accessToken: String): Boolean {
		return validateToken(accessToken, jwtAccessSecret)
	}

	fun validateRefreshToken(refreshToken: String): Boolean {
		return validateToken(refreshToken, jwtRefreshSecret)
	}

	private fun validateToken(token: String, secret: Key): Boolean {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secret)
				.build()
				.parseClaimsJws(token)
			return true
		} catch (expEx: ExpiredJwtException) {
			log.error("Token expired", expEx)
		} catch (unsEx: UnsupportedJwtException) {
			log.error("Unsupported jwt", unsEx)
		} catch (mjEx: MalformedJwtException) {
			log.error("Malformed jwt", mjEx)
		} catch (e: Exception) {
			log.error("invalid token", e)
		}
		return false
	}

	fun getAccessClaims(token: String): Claims {
		return getClaims(token, jwtAccessSecret)
	}

	fun getRefreshClaims(token: String): Claims {
		return getClaims(token, jwtRefreshSecret)
	}

	private fun getClaims(token: String, secret: Key): Claims {
		return Jwts.parserBuilder()
			.setSigningKey(secret)
			.build()
			.parseClaimsJws(token)
			.body
	}

	companion object {
		val log: Logger = LogManager.getLogger(JwtProvider::class.java.name)
	}
}