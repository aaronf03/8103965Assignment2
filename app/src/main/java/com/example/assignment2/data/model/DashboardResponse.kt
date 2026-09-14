package com.example.assignment2.data.model

/**
 * Response body for GET /dashboard/{keypass}.
 *
 * The individual entity property names (e.g. "name", "location", "price") vary
 * depending on the topic assigned to each student's keypass, but every entity
 * always includes a "description" field. Using Map<String, Any> instead of a
 * fixed data class keeps this app working regardless of which topic/dataset
 * is returned for a given student.
 */
data class DashboardResponse(
    val entities: List<Map<String, Any>>,
    val entityTotal: Int
)
