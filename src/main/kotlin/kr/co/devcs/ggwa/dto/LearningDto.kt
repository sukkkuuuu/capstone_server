package kr.co.devcs.ggwa.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LearningDto(
    @field:NotNull(message = "제목은 필수 항목입니다.")
    @field:NotBlank(message = "제목은 필수 항목입니다.")
    val title: String? = null,
    @field:NotNull(message = "소개는 필수 항목입니다.")
    @field:NotBlank(message = "소개는 필수 항목입니다.")
    val intro: String? = null,
    @field:NotNull(message = "소개는 필수 항목입니다.")
    @field:NotBlank(message = "소개는 필수 항목입니다.")
    val address: String? = null
)