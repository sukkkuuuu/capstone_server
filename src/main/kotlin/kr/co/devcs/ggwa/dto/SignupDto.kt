package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SignupDto(
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val universityName: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val email: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val password1: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val password2: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val sno: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val nickname: String? = null,
    @field:NotNull(message = "필수 항목입니다.")
    @field:NotBlank(message = "필수 항목입니다.")
    val birthDate: String? = null
)