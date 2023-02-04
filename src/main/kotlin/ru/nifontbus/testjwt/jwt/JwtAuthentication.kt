package ru.nifontbus.testjwt.jwt

import org.springframework.security.core.Authentication
import ru.nifontbus.testjwt.model.Role


class JwtAuthentication : Authentication {

	private var authenticated = false
	var username: String = ""
	var firstName: String = ""
	var roles: Set<Role> = emptySet()

	override fun getName(): String {
		return firstName
	}

	override fun getAuthorities(): Set<Role> {
		return roles
	}

	override fun getCredentials(): Any? {
		return null
	}

	override fun getDetails(): Any? {
		return null
	}

	override fun getPrincipal(): Any {
		return username
	}

	override fun isAuthenticated(): Boolean {
		return authenticated
	}

	override fun setAuthenticated(isAuthenticated: Boolean) {
		this.authenticated = isAuthenticated
	}
}