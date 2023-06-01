package kr.co.devcs.ggwa.response

import kr.co.devcs.ggwa.entity.Comment
import kr.co.devcs.ggwa.entity.Meeting

data class MemberResponse(val data: MutableMap<String, String>, val errors: MutableList<String>)
data class UnivResponse(val data: MutableList<String>, val errors: MutableList<String>)
data class BoardResponse(val data: MutableList<MutableMap<String, String>>, val errors: MutableList<String>)

data class BoardDetailResponse(val data: MutableMap<String, Meeting>, val errors: MutableList<String>)

data class CommentResponse(val data: MutableList<Comment>, val errors: MutableList<String>)