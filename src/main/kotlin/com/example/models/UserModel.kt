package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val username = varchar("name", 1024)
    val google_token = varchar("google_token ", 1024).nullable()
    val github_token = varchar("github_token ", 1024).nullable()
    val facebook_token = varchar("facebook_token ", 1024).nullable()
    val admin = bool("admin").default(false)

}


class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var username by Users.username
    var cart by Product via UserCarts
    var admin by Users.admin

    var google_token by Users.google_token
    var github_token by Users.github_token
    var facebook_token by Users.facebook_token
}
