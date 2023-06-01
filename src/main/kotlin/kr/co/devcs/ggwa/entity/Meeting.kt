package kr.co.devcs.ggwa.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Meeting(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val count: Long,
    val intro: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val writer: Member
)