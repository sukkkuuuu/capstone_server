package kr.co.devcs.ggwa.service

import kr.co.devcs.ggwa.repository.UniversityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UniversityService(
    @Autowired private val universityRepository: UniversityRepository
) {
    fun isExistName(name: String) = universityRepository.existsByName(name)
    fun findByName(name: String) = universityRepository.findByName(name)
}