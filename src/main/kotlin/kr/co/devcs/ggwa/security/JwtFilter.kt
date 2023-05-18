package kr.co.devcs.ggwa.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.devcs.ggwa.util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    @Autowired private val jwtUtils: JwtUtils
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorization: String? = request.getHeader("Authorization") ?: return filterChain.doFilter(request, response)
        val token = authorization?.substring("Bearer ".length) ?: return filterChain.doFilter(request, response)
        if (jwtUtils.validateToken(token)) {
            val email = jwtUtils.getClaims(token)["email"] as String
            val authentication = jwtUtils.getAuthentication(email)
            if (authentication.isAuthenticated) SecurityContextHolder.getContext().authentication = authentication
        }
        return filterChain.doFilter(request, response)
    }
}