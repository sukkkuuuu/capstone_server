package kr.co.devcs.ggwa.entity

import jakarta.persistence.*

@Entity
class University(
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long? = null,

    @Id
    @Column(unique = true)
    val name: String,

    @Column(unique = true)
    val emailAddress: String,

    @Column(unique = true)
    val address: String
)