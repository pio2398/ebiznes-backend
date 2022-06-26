package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

data class UserCart(val id: Int)

object UserCarts : IntIdTable() {
    var user by Users referencedOn UserRatings.film // use referencedOn for normal references


}