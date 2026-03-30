package com.example.fadedstudioadmin

data class Appointment(
    var id: String? = null,
    val userEmail: String? = null,
    val serviceName: String? = null,
    var status: String? = "PENDING",
    var isUpdating: Boolean = false
)