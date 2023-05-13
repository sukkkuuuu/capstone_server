package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotNull

data class SigninDto(
    @NotNull(message = "이메일: 필수 항목입니다.")
    val email: String,
    @NotNull(message = "패스워드: 필수 항목입니다.")
    val password: String
)