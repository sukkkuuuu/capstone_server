package kr.co.devcs.ggwa.controller

import kr.co.devcs.ggwa.dto.AuthCodeDto
import kr.co.devcs.ggwa.dto.ProfileUpdateDto
import kr.co.devcs.ggwa.dto.SigninDto
import kr.co.devcs.ggwa.dto.SignupDto
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.security.MemberDetails
import kr.co.devcs.ggwa.service.MemberService
import kr.co.devcs.ggwa.util.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

data class MemberResponse(val data: MutableMap<String, String>, val errors: MutableList<String>)

@RestController
@RequestMapping("/api/member")
class MemberController(
    @Autowired private val memberService: MemberService,
) {
    @PostMapping("/signup")
    fun signup(@RequestBody @Validated signupDto: SignupDto, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if (memberService.checkEmailDuplication(signupDto.email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이미 있는 이메일입니다.")))
        if (memberService.checkNicknameDuplication(signupDto.nickname)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이미 있는 닉네임입니다.")))
        if (!memberService.confirmPassword(signupDto.password1, signupDto.password2)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("비밀번호와 확인 비밀번호가 일치하지 않습니다.")))
        memberService.signup(signupDto)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @PostMapping("/validAuthCode")
    fun validAuthCode(@RequestBody @Validated authCodeDto: AuthCodeDto, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if(!memberService.activate(authCodeDto.email, authCodeDto.authCode)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("인증 코드가 만료되었거나 일치하지 않습니다.")))
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @PostMapping("/signin")
    fun signin(@RequestBody @Validated signinDto: SigninDto, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if (!memberService.checkEmailDuplication(signinDto.email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("가입되지 않은 이메일입니다.")))
        if (!memberService.checkPassword(signinDto.email, signinDto.password)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("정확하지 않은 패스워드입니다.")))
        if (!memberService.isEnabledMember(signinDto.email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이메일 인증이 안된 계정입니다.")))
        val token = memberService.signin(signinDto)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("jwt" to "$token"), mutableListOf()))
    }

    @GetMapping("/profile/{nickname}")
    fun mypage(@PathVariable("nickname") nickname:String): ResponseEntity<MemberResponse> {
        if (!memberService.checkNicknameDuplication(nickname)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("가입되지 않은 이메일입니다.")))
        val member: Member = memberService.findByNickname(nickname)!!
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf(
                "email" to member.email, "nickname" to member.nickname, "sno" to member.sno,
                "birthDate" to member.birthDate.toString(), "createDate" to member.createdDate.toString()
        ), mutableListOf()))
    }

    @PatchMapping("/profile/update")
    fun update(@RequestBody @Validated profileUpdateDto: ProfileUpdateDto, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if (!memberService.checkEmailDuplication(profileUpdateDto.email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("수정 대상을 찾을 수 없습니다.")))
        val memberDetailts: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        if (memberDetailts.username != profileUpdateDto.email) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("수정 권한이 없습니다.")))
        memberService.update(profileUpdateDto)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @DeleteMapping("/profile/delete")
    fun delete(email:String): ResponseEntity<MemberResponse> {
        if (!memberService.checkEmailDuplication(email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("삭제 대상을 찾을 수 없습니다.")))
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        if (memberDetails.username != email) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("삭제 권한이 없습니다.")))
        memberService.delete(email)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }


    @GetMapping("/auth")
    fun test() = "suc"

    private fun fieldErrors(bindingResult: BindingResult): MutableList<String> {
        val errors = mutableListOf<String>()
        bindingResult.allErrors.forEach {
            val field = it as FieldError
            val message = it.defaultMessage
            errors.add("${field.field} : $message")
        }
        return errors
    }
}