package com.example.minecraft.api.dto

data class ServerPropertiesDto(
    val serverProperties: Map<String, String>,
){
    companion object{

        fun of(serverProperties: Map<String, String>): ServerPropertiesDto{
            return ServerPropertiesDto(serverProperties)
        }
    }
}