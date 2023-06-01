package kr.co.devcs.ggwa.service

import kr.co.devcs.ggwa.dto.MeetingDto
import kr.co.devcs.ggwa.entity.Meeting
import kr.co.devcs.ggwa.entity.Member
import kr.co.devcs.ggwa.repository.MeetingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class MeetingService(
    @Autowired private val meetingRepository: MeetingRepository
) {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 5
    }
    fun create(meetingDto: MeetingDto, writer: Member) {
        meetingRepository.save(Meeting(title = meetingDto.title!!, count = meetingDto.count!!, intro = meetingDto.intro!!, writer = writer))
    }

    fun delete(id: Long) {
        val meeting = meetingRepository.findById(id).get()
        meetingRepository.delete(meeting)
    }

    fun findAll(page: Int, sort: String, order: String):Page<Meeting> {
        val pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(if(order == "asc") Sort.Direction.ASC else Sort.Direction.DESC, sort))
        return meetingRepository.findAll(pageable)
    }

    fun checkById(id: Long) = meetingRepository.existsById(id)

    fun findById(id: Long): Meeting{
        return meetingRepository.findById(id).get()
    }

}