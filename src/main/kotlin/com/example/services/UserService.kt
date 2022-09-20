package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.helpers.httpClient
import com.example.models.User
import com.example.models.Users
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

@Serializable
private data class GoogleUserInfo(
    val id: String,
    val name: String,
    val given_name: String,
    val family_name: String,
    val picture: String,
    val locale: String,
    val email: String
)

@Serializable
private data class GithubUserInfo(
    val id: Long,
    val avatar_url: String,
    val login: String,
    val email: String?
)

@Serializable
private data class FacebookPicture(
    val height: Int,
    val width: Int,
    val is_silhouette: Boolean,
    val url: String
)

@Serializable
private data class FacebookPictureContainer(
    @SerialName("data")
    val picture: FacebookPicture

)

@Serializable
private data class FacebookUserInfo(
    val id: String,
    val name: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val picture: FacebookPictureContainer
)

@Serializable
data class UserResponse(
    val id: Int,
    var admin: Boolean,
    var display_name: String,
) {
    constructor(user: User) : this(user.id.value, user.admin, user.username)
}

@Serializable
data class RedirectUrlSession(val url: String)

interface UserService {
    suspend fun loginFacebook(accessToken: String): UserResponse;
    suspend fun loginGoogle(accessToken: String): UserResponse;
    suspend fun loginGithub(accessToken: String): UserResponse;
    fun createToken(user: UserResponse): String
}


class UserServiceImpl(private val databaseFactory: DatabaseFactory, private val settingsService: SettingsService) :
    UserService {

    override suspend fun loginFacebook(accessToken: String): UserResponse {
        val userInfo: FacebookUserInfo = httpClient.get(
            "https://graph.facebook.com/me?fields=id,name,email,picture,first_name,last_name&access_token=${accessToken}"
        ).body()
        var currentUser: User? = null;
        transaction(databaseFactory.dataBase) {
            currentUser = User.find(Users.facebook_token eq userInfo.id).firstOrNull()
        }
        if (currentUser != null) {
            return UserResponse(currentUser!!)
        }
        var userId: EntityID<Int>? = null
        transaction(databaseFactory.dataBase) {

            userId = Users.insert {
                it[Users.username] = userInfo.name
                it[Users.facebook_token] = userInfo.id
            } get Users.id
        }
        return UserResponse(id = userId!!.value, admin = false, display_name = userInfo.name)

    }


    override suspend fun loginGoogle(accessToken: String): UserResponse {

        val userInfo: GoogleUserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.body()
        var userId: EntityID<Int>? = null
        transaction(databaseFactory.dataBase) {


            val currentUser = User.find(Users.google_token eq userInfo.id).firstOrNull()
            if (currentUser != null) {
                return@transaction UserResponse(currentUser)
            }

            userId = Users.insert {
                it[Users.username] = userInfo.name
                it[Users.google_token] = userInfo.id
            } get Users.id
        }

        return UserResponse(id = userId!!.value, admin = false, display_name = userInfo.name)
    }

    override suspend fun loginGithub(accessToken: String): UserResponse {
        val userInfo: GithubUserInfo = httpClient.get("https://api.github.com/user") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.body()

        val githubId = userInfo.id.toString()
        val currentUser = User.find(Users.github_token eq githubId).firstOrNull()
        if (currentUser != null) {
            return UserResponse(currentUser)
        }

        val userId = Users.insert {
            it[Users.username] = userInfo.login
            it[Users.github_token] = githubId
        } get Users.id

        return UserResponse(id = userId.value, admin = false, display_name = userInfo.login)
    }

    override fun createToken(user: UserResponse): String {
        return JWT.create()
            .withIssuer(settingsService.jwt.jwtIssuer)
            .withClaim("id", user.id)
            .withClaim("display_name", user.display_name)
            .withClaim("admin", user.admin)
            .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 24 * 3))
            .sign(Algorithm.HMAC256(settingsService.jwt.jwtSecret))
    }

}
