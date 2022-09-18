package com.example.attributes

import com.example.models.Attribute

@kotlinx.serialization.Serializable
data class AttributesDTO(val type: String) {
    constructor(attribute: Attribute) : this(attribute.name)
}
