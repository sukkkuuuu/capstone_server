package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.Learning
import org.springframework.data.jpa.repository.JpaRepository

interface LearningRepository: JpaRepository<Learning, Long> {
}