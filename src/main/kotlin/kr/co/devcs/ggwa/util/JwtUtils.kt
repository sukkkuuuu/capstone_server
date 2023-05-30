package kr.co.devcs.ggwa.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import kr.co.devcs.ggwa.security.MemberDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtils(
    @Autowired private val memberDetailsService: MemberDetailsService
) {
    val EXP_TIME = 1000L * 60 * 60
    val SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    fun createToken(email: String): String {
        val claims: Claims = Jwts.claims()
        claims["email"] = email
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date(System.currentTimeMillis() + EXP_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact()
    }

    fun validateToken(token: String): Boolean {
        val claims: Claims = getClaims(token)
        val exp: Date = claims.expiration
        return exp.after(Date())
    }

    fun getClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                .build().parseClaimsJws(token).body
    }

    fun getAuthentication(email: String): UsernamePasswordAuthenticationToken {
        val memberDetails = memberDetailsService.loadUserByUsername(email)
        val authentication = UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.authorities)
        if (!memberDetails.isEnabled) authentication.isAuthenticated = false
        return authentication
    }
}