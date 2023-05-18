package kr.co.devcs.ggwa.security

import kr.co.devcs.ggwa.entity.Authority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MemberDetails(
        private val email: String,
        private val password: String,
        private val isEnabled: Boolean,
        private val authorities: MutableSet<Authority>
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities.map { SimpleGrantedAuthority(it.name) }

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = isEnabled
}