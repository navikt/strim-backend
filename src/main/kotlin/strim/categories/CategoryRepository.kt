package strim.categories

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int> {
    fun findByNameIgnoreCaseIn(names: Collection<String>): List<Category>
}