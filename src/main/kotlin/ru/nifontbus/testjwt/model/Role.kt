package ru.nifontbus.testjwt.model

import org.springframework.security.core.GrantedAuthority


enum class Role(private val value: String) : GrantedAuthority {
	ADMIN("ADMIN"),
	USER("USER"),
	NONE("");

	override fun getAuthority(): String {
		return value
	}
}

