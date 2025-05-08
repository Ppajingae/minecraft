package com.example.minecraft.api.service

import com.example.minecraft.api.dto.ServerPropertiesDto
import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class FileService {

    fun getServerProperties(): BaseResponse<ServerPropertiesDto> {
        val file = File("/minecraft/server.properties")
        if (!file.exists()) throw IllegalArgumentException("file is not exists")

        val properties = file.readLines()
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .map { it.split("=", limit = 2) }
            .associate { it[0] to it.getOrElse(1) { "" } }

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(), ServerPropertiesDto.of(properties), "")
    }
}