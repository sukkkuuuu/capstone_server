package kr.co.devcs.ggwa.service

import kr.co.devcs.ggwa.dto.SigninDto
import kr.co.devcs.ggwa.dto.SignupDto
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.repository.MemberRepository
import kr.co.devcs.ggwa.util.JwtUtils
import kr.co.devcs.ggwa.util.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val mailService: MailService,
    @Autowired private val jwtUtils: JwtUtils,
    @Autowired private val passwordEncoder: PasswordEncoder
) {
    fun checkEmailDuplication(email: String) = memberRepository.existsByEmail(email)
    fun checkNicknameDuplication(nickname: String) = memberRepository.existsByNickname(nickname)
    fun confirmPassword(password1: String, password2: String) = password1 == password2
    fun isEnabledMember(email: String) = memberRepository.findByEmail(email)!!.isEnabled
    fun checkPassword(email: String, password: String) = passwordEncoder.matches(password, memberRepository.findByEmail(email)!!.password)

    fun signup(signupDto: SignupDto) {
        mailService.sendEmailForm(signupDto.email, signupDto.nickname)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        memberRepository.save(Member(
            sno = signupDto.sno,
            email = signupDto.email,
            nickname = signupDto.nickname,
            password = passwordEncoder.encode(signupDto.password1),
            birthDate = LocalDate.parse(signupDto.birthDate, formatter)
        ))
    }

    fun signin(signinDto: SigninDto) = jwtUtils.createToken(signinDto.email)


    fun activate(email: String, authCode: String): Boolean {
        if(mailService.isValidEmailCode(email, authCode)) {
            val member: Member? = memberRepository.findByEmail(email) ?: throw UsernameNotFoundException("인증 정보 오류")
            member!!.isEnabled = true
            memberRepository.save(member)
            return true
        }
        return false
    }
}