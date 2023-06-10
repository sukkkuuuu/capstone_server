package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.Learning
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LearningRepository: JpaRepository<Learning, Long> {
    fun findByWriter(writer: Member, pageable: Pageable): Page<Learning>
}