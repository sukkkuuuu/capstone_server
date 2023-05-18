package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.Authority
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository: JpaRepository<Authority, Long> {
    fun findByName(name: String): Authority?
}