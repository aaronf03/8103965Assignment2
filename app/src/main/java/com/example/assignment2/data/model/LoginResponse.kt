package com.example.assignment2.data.model

/**
 * Response body for POST /{campus}/auth on success.
 * "keypass" identifies the topic/dataset used for the dashboard endpoint.
 */
data class LoginResponse(
    val keypass: String
)
