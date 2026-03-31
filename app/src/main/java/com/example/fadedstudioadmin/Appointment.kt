package com.example.fadedstudioadmin

// Make sure your fields match this!
data class Appointment(
    val appointmentId: String = "",
    val userId: String = "",
    val serviceName: String = "",
    val barberName: String = "", // Added Barber!
    val dateTime: String = "",
    val status: String = ""
)