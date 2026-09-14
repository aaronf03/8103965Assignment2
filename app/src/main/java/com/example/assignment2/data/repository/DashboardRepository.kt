package com.example.assignment2.data.repository

import com.example.assignment2.data.remote.ApiService
import com.example.assignment2.util.Resource
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

/**
 * Handles Dashboard API requests.
 */
class DashboardRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getDashboard(keypass: String): Resource<List<Map<String, String>>> {
        return try {
            val response = apiService.getDashboard(keypass)
            val entities = response.entities.map { entity ->
                entity.mapValues { it.value.toString() }
            }
            Resource.Success(entities)
        } catch (e: HttpException) {
            Resource.Error("Failed to load dashboard: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Network error: please check your internet connection.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred while loading the dashboard.")
        }
    }
}
