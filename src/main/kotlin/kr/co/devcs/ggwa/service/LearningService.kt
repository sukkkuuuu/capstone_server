package kr.co.devcs.ggwa.service

import kr.co.devcs.ggwa.dto.LearningDto
import kr.co.devcs.ggwa.dto.MeetingDto
import kr.co.devcs.ggwa.entity.Learning
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.repository.LearningRepository
import kr.co.devcs.ggwa.repository.MeetingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class LearningService(
    @Autowired private val learningRepository: LearningRepository
) {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 5
    }

    fun create(learningDto: LearningDto, writer: Member) {
        learningRepository.save(Learning(title = learningDto.title!!, intro = learningDto.intro!!, writer = writer, address = learningDto.address!!))
    }

    fun delete(id: Long) {
        val learning = learningRepository.findById(id).get()
        learningRepository.delete(learning)
    }

    fun findAll(page: Int, sort: String, order: String): Page<Learning> {
        val pageable = PageRequest.of(
            page,
            DEFAULT_PAGE_SIZE,
            Sort.by(if (order == "asc") Sort.Direction.ASC else Sort.Direction.DESC, sort)
        )
        return learningRepository.findAll(pageable)
    }

    fun findByWriter(writer: Member, page: Int, sort: String, order: String): Page<Learning> {
        val pageable = PageRequest.of(page,
            LearningService.DEFAULT_PAGE_SIZE, Sort.by(if(order == "asc") Sort.Direction.ASC else Sort.Direction.DESC, sort))
        return learningRepository.findByWriter(writer, pageable)
    }
    fun checkById(id: Long) = learningRepository.existsById(id)

    fun findById(id: Long): Learning {
        return learningRepository.findById(id).get()
    }
}