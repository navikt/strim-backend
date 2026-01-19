package strim.categories

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryRepository: CategoryRepository
) {
    @GetMapping
    fun getAll(): List<CategoryDTO> =
        categoryRepository.findAll()
            .sortedBy { it.name }
            .map { CategoryDTO(it.id, it.name) }
}
