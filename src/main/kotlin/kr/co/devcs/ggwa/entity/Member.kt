package kr.co.devcs.ggwa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    var sno: String,
    @Column(unique = true)
    val email: String,
    @Column(unique = true)
    var nickname: String,
    var password: String,
    var birthDate: LocalDate,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    var isEnabled: Boolean = false
)