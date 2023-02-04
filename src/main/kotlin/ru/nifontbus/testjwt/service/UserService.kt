package ru.nifontbus.testjwt.service

import org.springframework.stereotype.Service
import ru.nifontbus.testjwt.model.Role
import ru.nifontbus.testjwt.model.User

@Service
class UserService {
	private val users = listOf(
		User("anton", "1234", "Антон", "Иванов", setOf(Role.USER)),
		User("ivan", "12345", "Сергей", "Петров", setOf(Role.ADMIN))
	)

	fun getByLogin(login: String): User? {
		return users.first { it.login == login }
	}
}