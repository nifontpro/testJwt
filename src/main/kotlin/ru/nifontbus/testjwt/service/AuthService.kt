package ru.nifontbus.testjwt.service

import io.jsonwebtoken.Claims
import jakarta.security.auth.message.AuthException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.nifontbus.testjwt.jwt.JwtAuthentication
import ru.nifontbus.testjwt.model.JwtRequest
import ru.nifontbus.testjwt.model.JwtResponse
import ru.nifontbus.testjwt.model.User

@Service
class AuthService(
	private val userService: UserService,
	private val jwtProvider: JwtProvider
) {

	private val refreshStorage: MutableMap<String, String> = HashMap()

	fun login(authRequest: JwtRequest): JwtResponse {
		val user: User = userService.getByLogin(authRequest.login) ?: throw AuthException("Пользователь не найден")
		return if (user.password == authRequest.password) {
			val accessToken: String = jwtProvider.generateAccessToken(user)
			val refreshToken: String = jwtProvider.generateRefreshToken(user)
			refreshStorage[user.login] = refreshToken
			JwtResponse(accessToken, refreshToken)
		} else {
			throw AuthException("Неправильный пароль")
		}
	}

	fun getAccessToken(refreshToken: String): JwtResponse {
		if (jwtProvider.validateRefreshToken(refreshToken)) {
			val claims: Claims = jwtProvider.getRefreshClaims(refreshToken)
			val login = claims.subject
			val saveRefreshToken = refreshStorage[login]
			if (saveRefreshToken != null && saveRefreshToken == refreshToken) {
				val user: User = userService.getByLogin(login) ?: throw AuthException("Пользователь не найден")
				val accessToken: String = jwtProvider.generateAccessToken(user)
				return JwtResponse(accessToken, null)
			}
		}
		return JwtResponse(null, null)
	}

	fun refresh(refreshToken: String): JwtResponse {
		if (jwtProvider.validateRefreshToken(refreshToken)) {
			val claims: Claims = jwtProvider.getRefreshClaims(refreshToken)
			val login = claims.subject
			val saveRefreshToken = refreshStorage[login]
			if (saveRefreshToken != null && saveRefreshToken == refreshToken) {
				val user: User = userService.getByLogin(login) ?: throw AuthException("Пользователь не найден")
				val accessToken: String = jwtProvider.generateAccessToken(user)
				val newRefreshToken: String = jwtProvider.generateRefreshToken(user)
				refreshStorage[user.login] = newRefreshToken
				return JwtResponse(accessToken, newRefreshToken)
			}
		}
		throw AuthException("Невалидный JWT токен")
	}

	val authInfo: JwtAuthentication
		get() = SecurityContextHolder.getContext().authentication as JwtAuthentication
}