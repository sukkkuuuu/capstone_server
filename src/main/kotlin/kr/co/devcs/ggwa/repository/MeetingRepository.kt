package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.Meeting
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MeetingRepository: JpaRepository<Meeting, Long> {
}