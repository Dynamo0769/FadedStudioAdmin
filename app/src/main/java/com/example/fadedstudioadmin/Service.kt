package com.example.fadedstudioadmin
import java.io.Serializable

data class Service(
    val serviceId: String = "",
    val name: String = "",
    val detail: String = "",
    val price: String = "",
    val duration: String = "",
    val imageUrl: String = ""
) : Serializable