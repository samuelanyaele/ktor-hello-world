package com.example.auth

import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode

object JwtConfig {
    val secret = System.getenv("JWT_SECRET")
        ?: "your-secret-key-change-in-production"

    val issuer = "task-manager"
    val audience = "task-manager-users"
    val realm = "Task Manager API"
    val expirationMs = 3600000L
}

fun Application.configureJwt() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.realm

            verifier(
                JWT.require(Algorithm.HMAC256(JwtConfig.secret))
                    .withIssuer(JwtConfig.issuer)
                    .withAudience(JwtConfig.audience)
                    .build()
            )

            validate { credential ->
                val username = credential.payload
                    .getClaim("username").asString()

                if (username.isNullOrBlank()) {
                    null
                } else {
                    JWTPrincipal(credential.payload)
                }
            }

            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Token is invalid or expired")
                )
            }
        }
    }
}