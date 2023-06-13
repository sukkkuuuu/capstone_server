package kr.co.devcs.ggwa.controller

import kr.co.devcs.ggwa.dto.CommentDto
import kr.co.devcs.ggwa.entity.Comment
import kr.co.devcs.ggwa.response.CommentResponse
import kr.co.devcs.ggwa.security.MemberDetails
import kr.co.devcs.ggwa.service.CommentService
import kr.co.devcs.ggwa.service.LearningService
import kr.co.devcs.ggwa.service.MeetingService
import kr.co.devcs.ggwa.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comment")
class CommentController(
    @Autowired private val commentService: CommentService,
    @Autowired private val meetingService: MeetingService,
    @Autowired private val learningService: LearningService,
    @Autowired private val memberService: MemberService
) {
    @GetMapping("meeting/{meeting_id}")
    fun m_comment_list(@PathVariable meeting_id: Long): ResponseEntity<CommentResponse> {
        if(!meetingService.checkById(meeting_id)) return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("비정상적인 접근입니다.")))
        val meeting = meetingService.findById(meeting_id)
        return ResponseEntity.ok().body(CommentResponse(commentService.findByMeeting(meeting)!!, mutableListOf()))
    }

    @PostMapping("meeting/{meeting_id}")
    fun m_comment_create(@PathVariable meeting_id: Long, @RequestBody commentDto: CommentDto): ResponseEntity<CommentResponse>{
        if(!meetingService.checkById(meeting_id)) return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("비정상적인 접근입니다.")))
        if(commentDto.content == "") return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("댓글을 작성해주세요.")))
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        val meeting = meetingService.findById(meeting_id)
        commentService.m_create(meeting, member!!, commentDto.content)
        return ResponseEntity.ok().body(CommentResponse(mutableListOf(), mutableListOf()))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<CommentResponse>{
        if(!commentService.checkById(id)) return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("비정상적인 접근입니다.")))
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        val comment = commentService.findById(id)
        if(comment.get().member != member) return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("비정상적인 접근입니다.")))
        commentService.delete(id)
        return ResponseEntity.ok().body(CommentResponse(mutableListOf(), mutableListOf()))
    }

    @GetMapping("learning/{learning_id}")
    fun l_comment_list(@PathVariable learning_id: Long): ResponseEntity<CommentResponse> {
        if(!learningService.checkById(learning_id)) return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("비정상적인 접근입니다.")))
        val learning = learningService.findById(learning_id)
        return ResponseEntity.ok().body(CommentResponse(commentService.findByLearning(learning)!!, mutableListOf()))
    }

    @PostMapping("learning/{learning_id}")
    fun l_comment_create(@PathVariable learning_id: Long, @RequestBody commentDto: CommentDto): ResponseEntity<CommentResponse>{
        if(!learningService.checkById(learning_id)) return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("비정상적인 접근입니다.")))
        if(commentDto.content == "") return ResponseEntity.badRequest().body(CommentResponse(mutableListOf(), mutableListOf("댓글을 작성해주세요.")))
        val memberDetails: MemberDetails = SecurityContextHolder.getContext().authentication.principal as MemberDetails
        val member = memberService.findByEmail(memberDetails.username)
        val learning = learningService.findById(learning_id)
        commentService.l_create(learning, member!!, commentDto.content)
        return ResponseEntity.ok().body(CommentResponse(mutableListOf(), mutableListOf()))
    }
}