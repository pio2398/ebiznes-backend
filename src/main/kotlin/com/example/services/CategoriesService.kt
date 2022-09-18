package com.example.services

import com.example.categories.dto.CategoryDTO
import com.example.models.Categories
import com.example.models.Category
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

interface CategoriesService {
    fun getCategoriesList(): List<CategoryDTO>
    fun getCategoriesListOnMainPage(): List<CategoryDTO>

    fun getCategory(category_id: Int): CategoryDTO?
    fun addCategory(category: CategoryDTO): CategoryDTO
}

class CategoriesServiceImpl(private val databaseFactory: DatabaseFactory) : CategoriesService {
    override fun getCategoriesList(): List<CategoryDTO> = transaction(databaseFactory.dataBase) {
        Category.all().map { CategoryDTO(it) }
    }

    override fun getCategoriesListOnMainPage(): List<CategoryDTO> = transaction(databaseFactory.dataBase) {
        Category.find(Categories.parent eq 1).map { CategoryDTO(it) }
    }

    override fun getCategory(category_id: Int): CategoryDTO? {
        val category = Category.findById(category_id)
        return if (category == null) null else CategoryDTO(category)
    }

    override fun addCategory(category: CategoryDTO): CategoryDTO = transaction(databaseFactory.dataBase) {
        val categoryId = Categories.insert {
            it[Categories.name] = category.name
            it[Categories.parent] = category.parent_id
        } get Categories.id

        category.copy(id = categoryId.value);
    }

}