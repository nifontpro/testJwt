package ru.nifontbus.testjwt.model

data class JwtResponse(
	val accessToken: String? = null,
	val refreshToken: String? = null,
)