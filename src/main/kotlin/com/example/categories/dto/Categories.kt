package com.example.categories.dto

import com.example.attributes.AttributesDTO

@kotlinx.serialization.Serializable
data class CategoryDTO(
    val name: String,
    val attributes: List<AttributesDTO> = listOf(),
    val id: Int? = null,
    val parent_id: Int? = null,

    ) {
    constructor(category: com.example.models.Category) : this(
        category.name, category.attributes.map { AttributesDTO(it) },
        category.id.value,
        category.parent?.id?.value
    )
}
