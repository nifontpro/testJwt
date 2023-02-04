package ru.nifontbus.testjwt.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import ru.nifontbus.testjwt.service.JwtProvider
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


@Component
class JwtFilter(
	private val jwtProvider: JwtProvider
) : GenericFilterBean() {

	override fun doFilter(request: ServletRequest, response: ServletResponse?, fc: FilterChain) {
		val token = getTokenFromRequest(request as HttpServletRequest)
		if (token != null && jwtProvider.validateAccessToken(token)) {
			val claims = jwtProvider.getAccessClaims(token)
			val jwtInfoToken: JwtAuthentication = JwtUtils.generate(claims)
			jwtInfoToken.isAuthenticated = true
			SecurityContextHolder.getContext().authentication = jwtInfoToken
		}
		fc.doFilter(request, response)
	}

	private fun getTokenFromRequest(request: HttpServletRequest): String? {
		val bearer = request.getHeader(AUTHORIZATION)
		return if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
			bearer.substring(7)
		} else null
	}

	companion object {
		private const val AUTHORIZATION = "Authorization"
	}
}