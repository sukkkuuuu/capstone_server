package kr.co.devcs.ggwa.util

import jakarta.servlet.http.HttpServletRequest
import kr.co.devcs.ggwa.response.MemberResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleHttpMessageNotReadableException(
            ex: HttpMessageNotReadableException,
            request: HttpServletRequest
    ): MemberResponse {
        val errorMessage = "JSON 문법을 확인해주세요."
        return MemberResponse(mutableMapOf(), mutableListOf(errorMessage))
    }
}