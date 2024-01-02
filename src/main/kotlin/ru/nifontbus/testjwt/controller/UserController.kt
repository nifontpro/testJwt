package ru.nifontbus.testjwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nifontbus.testjwt.jwt.JwtAuthentication
import ru.nifontbus.testjwt.service.AuthService


@RestController
@RequestMapping("api")

class UserController(
	private val authService: AuthService
) {

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("hello/user")
	fun helloUser(): ResponseEntity<String> {
		val authInfo: JwtAuthentication = authService.authInfo
		return ResponseEntity.ok("Hello user " + authInfo.principal + "!")
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("hello/admin")
	fun helloAdmin(): ResponseEntity<String> {
		val authInfo: JwtAuthentication = authService.authInfo
		return ResponseEntity.ok("Hello admin " + authInfo.principal + "!")
	}
}