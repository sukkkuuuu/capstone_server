package kr.co.devcs.ggwa.response

data class MemberResponse(val data: MutableMap<String, String>, val errors: MutableList<String>)
data class UnivResponse(val data: MutableList<String>, val errors: MutableList<String>)
