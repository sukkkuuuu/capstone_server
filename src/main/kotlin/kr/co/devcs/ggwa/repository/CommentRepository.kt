package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.Comment
import kr.co.devcs.ggwa.entity.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByMeeting(meeting: Meeting): MutableList<Comment>?
}