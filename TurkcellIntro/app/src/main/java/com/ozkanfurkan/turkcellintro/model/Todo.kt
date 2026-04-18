package com.ozkanfurkan.turkcellintro.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo (

    val id: Int,
    val title: String,
    val description: String? = null,
    //val completed: Boolean = false
    )
{

}