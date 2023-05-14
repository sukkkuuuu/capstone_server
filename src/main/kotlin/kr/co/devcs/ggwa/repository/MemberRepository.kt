package kr.co.devcs.ggwa.repository

import kr.co.devcs.ggwa.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long>{
    fun findByEmail(email: String): Member?
    fun findByNickname(nickname: String): Member?
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}