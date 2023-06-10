package kr.co.devcs.ggwa.controller

import kr.co.devcs.ggwa.dto.AuthCodeDto
import kr.co.devcs.ggwa.dto.ProfileUpdateDto
import kr.co.devcs.ggwa.dto.SigninDto
import kr.co.devcs.ggwa.dto.SignupDto
import kr.co.devcs.ggwa.entity.Learning
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.response.MeetingResponse
import kr.co.devcs.ggwa.response.MemberResponse
import kr.co.devcs.ggwa.security.MemberDetails
import kr.co.devcs.ggwa.service.LearningService
import kr.co.devcs.ggwa.service.MeetingService
import kr.co.devcs.ggwa.service.MemberService
import kr.co.devcs.ggwa.service.UniversityService
import kr.co.devcs.ggwa.util.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/member")
@CrossOrigin("*")
class MemberController(
    @Autowired private val memberService: MemberService,
    @Autowired private val universityService: UniversityService,
    @Autowired private val meetingService: MeetingService,
    @Autowired private val learningService: LearningService
) {
    @GetMapping("/")
    fun member(): Long {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        return member?.id!!
    }
    @PostMapping("/signup")
    fun signup(@RequestBody(required = false) @Validated signupDto: SignupDto?, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (signupDto == null) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("Body에 데이터가 없습니다.")))
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if (memberService.checkEmailDuplication(signupDto.email!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이미 있는 이메일입니다.")))
        if (memberService.checkNicknameDuplication(signupDto.nickname!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이미 있는 닉네임입니다.")))
        if (!memberService.confirmPassword(signupDto.password1!!, signupDto.password2!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("비밀번호와 확인 비밀번호가 일치하지 않습니다.")))
        if (!universityService.isExistName(signupDto.universityName!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("학교 정보가 올바르지 않습니다.")))
        memberService.signup(signupDto)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @PostMapping("/validAuthCode")
    fun validAuthCode(@RequestBody(required = false) @Validated authCodeDto: AuthCodeDto?, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (authCodeDto == null) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("Body에 데이터가 없습니다.")))
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if(!memberService.activate(authCodeDto.email!!, authCodeDto.authCode!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("인증 코드가 만료되었거나 일치하지 않습니다.")))
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @PostMapping("/signin")
    fun signin(@RequestBody @Validated signinDto: SigninDto?, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (signinDto == null) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("Body에 데이터가 없습니다.")))
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if (!memberService.checkEmailDuplication(signinDto.email!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("가입되지 않은 이메일입니다.")))
        if (!memberService.checkPassword(signinDto.email, signinDto.password!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("정확하지 않은 패스워드입니다.")))
        if (!memberService.isEnabledMember(signinDto.email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이메일 인증이 안된 계정입니다.")))
        val token = memberService.signin(signinDto)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("jwt" to "$token"), mutableListOf()))
    }

    @GetMapping("/profile/{nickname}")
    fun mypage(@PathVariable("nickname") nickname:String?): ResponseEntity<MemberResponse> {
        if (nickname == null) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("닉네임이 필요합니다.")))
        if (!memberService.checkNicknameDuplication(nickname)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("가입되지 않은 이메일입니다.")))
        val member: Member = memberService.findByNickname(nickname)!!
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf(
                "universityName" to member.university.name, "email" to member.email, "nickname" to member.nickname, "sno" to member.sno,
                "birthDate" to member.birthDate.toString(), "createDate" to member.createdDate.toString()
        ), mutableListOf()))
    }

    @PatchMapping("/profile/update")
    fun update(@RequestBody(required = false) @Validated profileUpdateDto: ProfileUpdateDto?, bindingResult: BindingResult): ResponseEntity<MemberResponse> {
        if (profileUpdateDto == null) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("Body에 데이터가 없습니다.")))
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), fieldErrors(bindingResult)))
        if (!memberService.checkEmailDuplication(profileUpdateDto.email!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("수정 대상을 찾을 수 없습니다.")))
        val memberDetailts: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        if (memberDetailts.username != profileUpdateDto.email) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("수정 권한이 없습니다.")))
//        if (memberService.checkNicknameDuplication(profileUpdateDto.nickname!!)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이미 있는 닉네임입니다.")))
        memberService.update(profileUpdateDto)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @DeleteMapping("/profile/delete")
    fun delete(email:String?): ResponseEntity<MemberResponse> {
        if (email == null) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("이메일이 필요합니다.")))
        if (!memberService.checkEmailDuplication(email)) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("삭제 대상을 찾을 수 없습니다.")))
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        if (memberDetails.username != email) return ResponseEntity.badRequest().body(MemberResponse(mutableMapOf(), mutableListOf("삭제 권한이 없습니다.")))
        memberService.delete(email)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf("success" to "true"), mutableListOf()))
    }

    @GetMapping("/checkToken")
    fun checkToken() = "suc"

    @GetMapping("/myinfo")
    fun myInfo(): ResponseEntity<MemberResponse> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        return ResponseEntity.ok().body(MemberResponse(mutableMapOf(
                "universityName" to member!!.university.name, "email" to member.email, "nickname" to member.nickname, "sno" to member.sno,
                "birthDate" to member.birthDate.toString(), "createDate" to member.createdDate.toString()
        ), mutableListOf()))
    }

    @GetMapping("/mypost/meeting")
    fun myPostMeeting(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "id") sort: String,
        @RequestParam(defaultValue = "asc") order: String
    ): Page<Meeting> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        return meetingService.findByWriter(member!!, page, sort, order)
    }

    @GetMapping("/mypost/learning")
    fun myPostLearning(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "id") sort: String,
        @RequestParam(defaultValue = "asc") order: String
    ): Page<Learning> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        return learningService.findByWriter(member!!, page, sort, order)
    }



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