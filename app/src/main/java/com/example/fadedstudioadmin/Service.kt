package com.example.fadedstudioadmin

data class Service(
    val id: String = "",
    val serviceName: String = "",
    val detail: String = "",
    val price: Double = 0.0,
    val duration: String = "",
    val imageUrl: String = "" // New field for the image link
)