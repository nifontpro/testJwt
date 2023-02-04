package ru.nifontbus.testjwt.model

data class User(
	val login: String = "",
	val password: String = "",
	val firstName: String = "",
	val lastName: String = "",
	val roles: Set<Role> = emptySet(),
)