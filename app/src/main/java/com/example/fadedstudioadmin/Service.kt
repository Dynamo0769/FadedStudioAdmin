package com.example.fadedstudioadmin
import java.io.Serializable

data class Service(
    var serviceId: String = "",
    var serviceName: String = "",
    var price: String = "",
    var duration: String = "",
    var imageUrl: String = ""
) : Serializable