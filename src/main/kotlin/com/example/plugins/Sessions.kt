package com.example.plugins

import com.example.services.RedirectUrlSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*


fun Application.configureCookies() {
    install(Sessions) {
        cookie<RedirectUrlSession>("redirect_url_session")
    }
}