package com.example.assignment2.data.repository

import com.example.assignment2.data.model.LoginRequest
import com.example.assignment2.data.remote.ApiService
import com.example.assignment2.util.Resource
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

/**
 * Mediates between the ViewModel layer and the remote API for authentication.
 * Constructor injection (via @Inject) lets Hilt provide this class automatically
 * wherever it is required, without any manual wiring.
 */
class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): Resource<String> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            Resource.Success(response.keypass)
        } catch (e: HttpException) {
            Resource.Error("Login failed: invalid username or password.")
        } catch (e: IOException) {
            Resource.Error("Network error: please check your internet connection.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred during login.")
        }
    }
}
