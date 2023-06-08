package kr.co.devcs.ggwa.response

import kr.co.devcs.ggwa.entity.Comment
import kr.co.devcs.ggwa.entity.Learning
import kr.co.devcs.ggwa.entity.Meeting

data class MemberResponse(val data: MutableMap<String, String>, val errors: MutableList<String>)
data class UnivResponse(val data: MutableList<String>, val errors: MutableList<String>)
data class MeetingResponse(val data: MutableList<MutableMap<String, String>>, val errors: MutableList<String>)
data class MeetingDetailResponse(val data: MutableMap<String, Meeting>, val errors: MutableList<String>)

data class LearningResponse(val data: MutableList<MutableMap<String, String>>, val errors: MutableList<String>)
data class LearningDetailResponse(val data: MutableMap<String, Learning>, val errors: MutableList<String>)


data class CommentResponse(val data: MutableList<Comment>, val errors: MutableList<String>)