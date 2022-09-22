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
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.concurrent.ConcurrentHashMap

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
    var username: String
) {
    constructor(user: User) : this(user.id.value, user.admin, user.username, user.display_name ?: user.username)
}

@Serializable
data class AuthSession(val token: String, val user: UserResponse)

interface UserService {
    suspend fun loginFacebook(accessToken: String): UserResponse
    suspend fun loginGoogle(accessToken: String): UserResponse
    suspend fun loginGithub(accessToken: String): UserResponse
    fun createToken(user: UserResponse): String
    fun userById(id: Int): User?
    fun createOneTimeToken(authSession: AuthSession): String;
    fun getByOneTimeToken(token: String): AuthSession?;
}


class UserServiceImpl(private val databaseFactory: DatabaseFactory, private val settingsService: SettingsService) :
    UserService {

    override suspend fun loginFacebook(accessToken: String): UserResponse {
        val userInfo: FacebookUserInfo = httpClient.get(
            "https://graph.facebook.com/me?fields=id,name,email,picture,first_name,last_name&access_token=${accessToken}"
        ).body()

        return transaction(databaseFactory.dataBase) {
            val currentUser = User.find(Users.facebook_token eq userInfo.id).firstOrNull()

            if (currentUser != null) {
                return@transaction UserResponse(currentUser)
            }
            val userId = Users.insert {
                it[username] = userInfo.name
                it[facebook_token] = userInfo.id
                it[display_name] = userInfo.first_name
            } get Users.id

            return@transaction UserResponse(
                id = userId.value,
                admin = false,
                display_name = userInfo.name,
                username = userInfo.first_name
            )
        }

    }


    override suspend fun loginGoogle(accessToken: String): UserResponse {

        val userInfo: GoogleUserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.body()

        return transaction(databaseFactory.dataBase) {

            val currentUser = User.find(Users.google_token eq userInfo.id).firstOrNull()
            if (currentUser != null) {
                return@transaction UserResponse(currentUser)
            }

            val userId = Users.insert {
                it[username] = userInfo.name
                it[google_token] = userInfo.id
            } get Users.id


            return@transaction UserResponse(
                id = userId.value,
                admin = false,
                display_name = userInfo.name,
                userInfo.given_name
            )
        }
    }

    override suspend fun loginGithub(accessToken: String): UserResponse {
        val userInfo: GithubUserInfo = httpClient.get("https://api.github.com/user") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.body()
        return transaction(databaseFactory.dataBase) {
            val githubId = userInfo.id.toString()
            val currentUser = User.find(Users.github_token eq githubId).firstOrNull()
            if (currentUser != null) {
                return@transaction UserResponse(currentUser)
            }

            val userId = Users.insert {
                it[username] = userInfo.login
                it[github_token] = githubId
            } get Users.id

            var isAdmin = false
            if (userInfo.login == "pio2398"){
                isAdmin = true;
            }

            return@transaction UserResponse(
                id = userId.value,
                admin = isAdmin,
                display_name = userInfo.login,
                username = userInfo.login
            )
        }
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

    override fun userById(id: Int): User? {
        return transaction(databaseFactory.dataBase) {
            return@transaction User.find(Users.id eq id).firstOrNull()
        }


    }

    var oneTimeToken: ConcurrentHashMap<String, AuthSession> = ConcurrentHashMap<String, AuthSession>()
    override fun createOneTimeToken(authSession: AuthSession): String {
        val token = java.util.UUID.randomUUID().toString();
        oneTimeToken[token] = authSession
        return token
    }

    override fun getByOneTimeToken(token: String): AuthSession? {
        return oneTimeToken[token]
    }

}
