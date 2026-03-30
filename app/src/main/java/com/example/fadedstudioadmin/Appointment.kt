package com.example.fadedstudioadmin

import java.io.Serializable

data class Appointment(
    var appointmentId: String = "",
    var userId: String = "", // Match User App's userId
    var serviceName: String = "",
    var dateTime: String = "", // Match User App's combined dateTime
    var status: String = "Pending"
) : Serializable