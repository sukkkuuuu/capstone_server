package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotNull

data class SignupDto(
    @field:NotNull(message = "학교: 필수 항목입니다.")
    val universityName: String,
    @field:NotNull(message = "이메일: 필수 항목입니다.")
    val email: String,
    @field:NotNull(message = "패스워드: 필수 항목입니다.")
    val password1: String,
    @field:NotNull(message = "확인 패스워드: 필수 항목입니다.")
    val password2: String,
    @field:NotNull(message = "학번: 필수 항목입니다.")
    val sno: String,
    @field:NotNull(message = "닉네임: 필수 항목입니다.")
    val nickname: String,
    @field:NotNull(message = "생일: 필수 항목입니다.")
    val birthDate: String
)