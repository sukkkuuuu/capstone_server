package kr.co.devcs.ggwa.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val content: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val meeting: Meeting,
    @ManyToOne
    val member: Member
)