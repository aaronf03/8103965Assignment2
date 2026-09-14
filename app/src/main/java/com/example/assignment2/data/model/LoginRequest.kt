package com.example.assignment2.data.model

/**
 * Request body for POST /{campus}/auth
 * username = student ID without the leading "s" (e.g. "8103965")
 * password = student's first name, case sensitive (e.g. "Aaron")
 */
data class LoginRequest(
    val username: String,
    val password: String
)
