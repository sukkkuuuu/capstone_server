package kr.co.devcs.ggwa.controller

import kr.co.devcs.ggwa.dto.MeetingDto
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.response.MeetingDetailResponse
import kr.co.devcs.ggwa.response.MeetingResponse
import kr.co.devcs.ggwa.security.MemberDetails
import kr.co.devcs.ggwa.service.MeetingService
import kr.co.devcs.ggwa.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/meeting")
@CrossOrigin("*")
class MeetingController(
    @Autowired private val meetingService: MeetingService,
    @Autowired private val memberService: MemberService
) {
    @PostMapping("/create")
    fun create(@RequestBody(required = false) @Validated meetingDto: MeetingDto, bindingResult: BindingResult): ResponseEntity<MeetingResponse> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username) ?: ResponseEntity.badRequest().body(MeetingResponse(mutableListOf(), mutableListOf("로그인 정보가 존재하지 않습니다.")))
        if (meetingDto == null) return ResponseEntity.badRequest().body(MeetingResponse(mutableListOf(), mutableListOf("Body에 데이터가 없습니다.")))
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(MeetingResponse(mutableListOf(), fieldErrors(bindingResult)))
        meetingService.create(meetingDto, member as Member)
        return ResponseEntity.ok().body(MeetingResponse(mutableListOf(mutableMapOf("success" to "true")), mutableListOf()))
    }

    @GetMapping("/boards")
    fun boards(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "id") sort: String,
        @RequestParam(defaultValue = "asc") order: String
    ): Page<Meeting> {
        return meetingService.findAll(page, sort, order)
    }

    @GetMapping("/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<MeetingDetailResponse> {
        if(!meetingService.checkById(id)) return ResponseEntity.badRequest().body(MeetingDetailResponse(mutableMapOf(), mutableListOf("존재하지 않는 게시물입니다.")))
        return ResponseEntity.ok().body(MeetingDetailResponse(mutableMapOf("data" to meetingService.findById(id)), mutableListOf()))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<MeetingDetailResponse> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username) ?: return ResponseEntity.badRequest().body(MeetingDetailResponse(mutableMapOf(), mutableListOf("로그인 정보가 존재하지 않습니다.")))
        val meeting = meetingService.findById(id)
        if(meeting.writer.id == member.id) {
            meetingService.delete(id)
            return ResponseEntity.ok().body(MeetingDetailResponse(mutableMapOf(), mutableListOf()))
        }
        return ResponseEntity.badRequest().body(MeetingDetailResponse(mutableMapOf(), mutableListOf("접근 권한이 없습니다.")))

    }

    private fun fieldErrors(bindingResult: BindingResult): MutableList<String> {
        val errors = mutableListOf<String>()
        bindingResult.allErrors.forEach {
            val message = it.defaultMessage
            errors.add("$message")
        }
        return errors
    }
}