package com.example.categories.dto

@kotlinx.serialization.Serializable
data class CategoryDTO(
    val name: String,
    val id: Int? = null,
    val parent_id: Int? = null,
) {
    constructor(category: com.example.models.Category) : this(
        category.name, category.id.value,
        category.parent?.id?.value
    )
}
