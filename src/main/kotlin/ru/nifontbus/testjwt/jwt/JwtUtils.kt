package ru.nifontbus.testjwt.jwt

import io.jsonwebtoken.Claims
import ru.nifontbus.testjwt.model.Role

object JwtUtils {
	fun generate(claims: Claims): JwtAuthentication {
		val jwtInfoToken = JwtAuthentication().apply {
			roles = getRoles(claims)
			firstName = claims["firstName", String::class.java]
			username = claims.subject
		}
		return jwtInfoToken
	}

	private fun getRoles(claims: Claims): Set<Role> {
		return claims.get("roles", List::class.java).map {
			try {
				Role.valueOf(it.toString())
			} catch (e: Exception) {
				Role.NONE
			}
		}.toSet()
	}
}