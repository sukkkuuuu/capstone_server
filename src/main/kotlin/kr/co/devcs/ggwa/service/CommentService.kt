package kr.co.devcs.ggwa.service

import kr.co.devcs.ggwa.entity.Comment
import kr.co.devcs.ggwa.entity.Learning
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
    fun findByLearning(learning: Learning) = commentRepository.findByLearning(learning)
    fun findById(id: Long) = commentRepository.findById(id)
    fun checkById(id: Long) = commentRepository.existsById(id)
    fun m_create(meeting: Meeting, member: Member, content: String) {
        val comment: Comment = Comment(
            content = content,
            meeting = meeting,
            member = member,
            learning = null
            )
        commentRepository.save(comment)
    }

    fun l_create(learning: Learning, member: Member, content: String) {
        val comment: Comment = Comment(
                content = content,
                meeting = null,
                member = member,
                learning = learning
        )
        commentRepository.save(comment)
    }
    fun delete(id: Long) {
        commentRepository.deleteById(id)
    }
}