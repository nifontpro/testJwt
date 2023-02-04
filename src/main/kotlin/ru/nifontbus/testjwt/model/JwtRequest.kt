package ru.nifontbus.testjwt.model

data class JwtRequest(
	val login: String = "",
	val password: String = "",
)
