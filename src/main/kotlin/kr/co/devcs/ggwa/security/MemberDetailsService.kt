package kr.co.devcs.ggwa.security

import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberDetailsService(
    @Autowired private val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val member: Member? = memberRepository.findByEmail(email) ?: throw UsernameNotFoundException("인증 불가 유저")
        return MemberDetails(member!!.email, member.password, member.isEnabled)
    }
}