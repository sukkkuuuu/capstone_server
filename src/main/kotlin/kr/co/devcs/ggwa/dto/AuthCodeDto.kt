package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AuthCodeDto(
    @field:NotNull(message = "이메일: 필수 항목입니다.") @field:NotBlank(message = "이메일: 필수 항목입니다.")
    val email: String? = null,
    @field:NotNull(message = "인증코드: 필수 항목입니다.") @field:NotBlank(message = "인증코드: 필수 항목입니다.")
    val authCode: String? = null
)