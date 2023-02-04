package ru.nifontbus.testjwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nifontbus.testjwt.model.JwtRequest
import ru.nifontbus.testjwt.model.JwtResponse
import ru.nifontbus.testjwt.model.RefreshJwtRequest
import ru.nifontbus.testjwt.service.AuthService

@RestController
@RequestMapping("api/auth")
class AuthController(
	private val authService: AuthService
) {

	@PostMapping("login")
	fun login(@RequestBody authRequest: JwtRequest): ResponseEntity<JwtResponse> {
		val token: JwtResponse = authService.login(authRequest)
		return ResponseEntity.ok(token)
	}

	@PostMapping("token")
	fun getNewAccessToken(@RequestBody request: RefreshJwtRequest): ResponseEntity<JwtResponse> {
		val token: JwtResponse = authService.getAccessToken(request.refreshToken)
		return ResponseEntity.ok(token)
	}

	@PostMapping("refresh")
	fun getNewRefreshToken(@RequestBody request: RefreshJwtRequest): ResponseEntity<JwtResponse> {
		val token: JwtResponse = authService.refresh(request.refreshToken)
		return ResponseEntity.ok(token)
	}
}