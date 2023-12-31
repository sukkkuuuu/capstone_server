package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SigninDto(
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val email: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    @field:NotBlank
    val password: String? = null
)