package kr.co.devcs.ggwa.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Learning(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val intro: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val writer: Member,
    val address: String
)