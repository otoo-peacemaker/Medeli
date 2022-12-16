package com.peacecodetech.medeli.data.request

data class HealthAssistants(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val user: User,
    val pharmacists: Pharmacists
)
