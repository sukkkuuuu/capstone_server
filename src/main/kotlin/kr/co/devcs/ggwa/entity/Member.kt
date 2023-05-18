package kr.co.devcs.ggwa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var sno: String,
    @Column(unique = true)
    val email: String,
    @Column(unique = true)
    var nickname: String,
    var password: String,
    var birthDate: LocalDate,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    var isEnabled: Boolean = false,
    @ManyToOne
    val university: University,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "member_authority",
        joinColumns = [JoinColumn(name = "member_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    val authorites: MutableSet<Authority> = mutableSetOf()
)