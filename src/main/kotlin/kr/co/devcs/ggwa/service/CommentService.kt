package kr.co.devcs.ggwa.service

import kr.co.devcs.ggwa.entity.Comment
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService(
    @Autowired private val commentRepository: CommentRepository
) {
    fun findByMeeting(meeting: Meeting) = commentRepository.findByMeeting(meeting)
    fun create(meeting: Meeting, member: Member, content: String) {
        val comment: Comment = Comment(
            content = content,
            meeting = meeting,
            member = member
            )
        commentRepository.save(comment)
    }
}