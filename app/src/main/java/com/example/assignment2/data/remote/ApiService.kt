package com.example.assignment2.data.remote

import com.example.assignment2.data.model.DashboardResponse
import com.example.assignment2.data.model.LoginRequest
import com.example.assignment2.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit definition for the nit3213api.
 *
 * IMPORTANT: The auth endpoint path depends on which campus/class you belong to.
 * Change "footscray" below to "sydney" or "br" if that applies to you.
 */
interface ApiService {

    @POST("footscray/auth")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
