package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotNull

data class ProfileUpdateDto(
    @field:NotNull(message = "이메일: 필수 항목입니다.")
    val email: String,
    val sno: String?,
    val nickname: String?,
    val birthDate: String?
)