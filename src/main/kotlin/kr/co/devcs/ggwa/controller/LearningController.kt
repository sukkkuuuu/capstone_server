package kr.co.devcs.ggwa.controller

import kr.co.devcs.ggwa.dto.LearningDto
import kr.co.devcs.ggwa.dto.MeetingDto
import kr.co.devcs.ggwa.entity.Learning
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.response.LearningDetailResponse
import kr.co.devcs.ggwa.response.LearningResponse
import kr.co.devcs.ggwa.security.MemberDetails
import kr.co.devcs.ggwa.service.LearningService
import kr.co.devcs.ggwa.service.MeetingService
import kr.co.devcs.ggwa.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/learning")
@CrossOrigin("*")
class LearningController(
    @Autowired private val learningService: LearningService,
    @Autowired private val memberService: MemberService
) {
    @PostMapping("/create")
    fun create(@RequestBody(required = false) @Validated learningDto: LearningDto, bindingResult: BindingResult): ResponseEntity<LearningResponse> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username) ?: ResponseEntity.badRequest().body(LearningResponse(mutableListOf(), mutableListOf("로그인 정보가 존재하지 않습니다.")))
        if (learningDto == null) return ResponseEntity.badRequest().body(LearningResponse(mutableListOf(), mutableListOf("Body에 데이터가 없습니다.")))
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(LearningResponse(mutableListOf(), fieldErrors(bindingResult)))
        learningService.create(learningDto, member as Member)
        return ResponseEntity.ok().body(LearningResponse(mutableListOf(mutableMapOf("success" to "true")), mutableListOf()))
    }

    @GetMapping("/boards")
    fun boards(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "id") sort: String,
        @RequestParam(defaultValue = "asc") order: String
    ): Page<Learning> {
        return learningService.findAll(page, sort, order)
    }

    @GetMapping("/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<LearningDetailResponse> {
        if(!learningService.checkById(id)) return ResponseEntity.badRequest().body(LearningDetailResponse(mutableMapOf(), mutableListOf("존재하지 않는 게시물입니다.")))
        return ResponseEntity.ok().body(LearningDetailResponse(mutableMapOf("data" to learningService.findById(id)), mutableListOf()))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<LearningDetailResponse> {
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username) ?: return ResponseEntity.badRequest().body(
            LearningDetailResponse(mutableMapOf(), mutableListOf("로그인 정보가 존재하지 않습니다."))
        )
        val learning = learningService.findById(id)
        if(learning.writer.id == member.id) {
            learningService.delete(id)
            return ResponseEntity.ok().body(LearningDetailResponse(mutableMapOf(), mutableListOf()))
        }
        return ResponseEntity.badRequest().body(LearningDetailResponse(mutableMapOf(), mutableListOf("접근 권한이 없습니다.")))

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