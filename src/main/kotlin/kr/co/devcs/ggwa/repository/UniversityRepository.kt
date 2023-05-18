package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.University
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UniversityRepository: JpaRepository<University, Long> {
    fun findByName(name: String): University?
    fun existsByName(name: String): Boolean
    override fun findAll(): MutableList<University>
}