package kr.co.devcs.ggwa.service

import jakarta.transaction.Transactional
import kr.co.devcs.ggwa.dto.ProfileUpdateDto
import kr.co.devcs.ggwa.dto.SigninDto
import kr.co.devcs.ggwa.dto.SignupDto
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.entity.University
import kr.co.devcs.ggwa.repository.AuthorityRepository
import kr.co.devcs.ggwa.repository.MemberRepository
import kr.co.devcs.ggwa.repository.UniversityRepository
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
    @Autowired private val universityService: UniversityService,
    @Autowired private val authorityRepository: AuthorityRepository,
    @Autowired private val mailService: MailService,
    @Autowired private val jwtUtils: JwtUtils,
    @Autowired private val passwordEncoder: PasswordEncoder
) {
    fun checkEmailDuplication(email: String) = memberRepository.existsByEmail(email)
    fun checkNicknameDuplication(nickname: String) = memberRepository.existsByNickname(nickname)
    fun confirmPassword(password1: String, password2: String) = password1 == password2
    fun isEnabledMember(email: String) = memberRepository.findByEmail(email)!!.isEnabled
    fun checkPassword(email: String, password: String) = passwordEncoder.matches(password, memberRepository.findByEmail(email)!!.password)

    fun findByNickname(email: String) = memberRepository.findByNickname(email)

    @Transactional
    fun signup(signupDto: SignupDto) {
        mailService.sendEmailForm(signupDto.email!!, signupDto.nickname!!)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val university = universityService.findByName(signupDto.universityName!!)
        val authority = authorityRepository.findByName("ROLE_USER") ?: throw Exception("ROLE_USER 테이블 확인해보세요.")
        memberRepository.save(Member(
            sno = signupDto.sno!!,
            email = signupDto.email!!,
            nickname = signupDto.nickname!!,
            password = passwordEncoder.encode(signupDto.password1),
            birthDate = LocalDate.parse(signupDto.birthDate, formatter),
            university = university!!,
            authorites = mutableSetOf(authority)
        ))
    }

    fun signin(signinDto: SigninDto) = jwtUtils.createToken(signinDto.email!!)

    @Transactional
    fun update(profileUpdateDto: ProfileUpdateDto) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val member = memberRepository.findByEmail(profileUpdateDto.email!!)!!
        member.sno = profileUpdateDto.sno ?: member.sno
        member.nickname = profileUpdateDto.nickname ?: member.nickname
        member.birthDate = LocalDate.parse(profileUpdateDto.birthDate ?: member.birthDate.toString(), formatter)
        memberRepository.save(member)
    }

    fun delete(email: String) {
        val member = memberRepository.findByEmail(email)
        memberRepository.delete(member!!)
    }

    fun activate(email: String, authCode: String): Boolean {
        if(mailService.isValidEmailCode(email, authCode)) {
            val member: Member = memberRepository.findByEmail(email) ?: throw UsernameNotFoundException("인증 정보 오류")
            member!!.isEnabled = true
            memberRepository.save(member)
            return true
        }
        return false
    }
}