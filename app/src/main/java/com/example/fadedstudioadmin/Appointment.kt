package com.example.fadedstudioadmin

data class Appointment(
    var appointmentId: String = "",
    var serviceName: String = "",
    var price: Double = 0.0,
    var status: String = "Pending",
    var userEmail: String = ""
)