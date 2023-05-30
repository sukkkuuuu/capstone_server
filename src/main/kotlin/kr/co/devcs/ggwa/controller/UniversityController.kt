package kr.co.devcs.ggwa.controller
import kr.co.devcs.ggwa.response.UnivResponse
import kr.co.devcs.ggwa.service.UniversityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/univ")
@CrossOrigin("*")
class UniversityController(
    @Autowired private val universityService: UniversityService
) {
    @GetMapping("/list")
    fun list() = UnivResponse(universityService.findAll().map { it.toString() }.toMutableList(), mutableListOf())
}