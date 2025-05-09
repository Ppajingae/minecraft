package com.example.minecraft.api.service

import com.example.minecraft.api.dto.ServerPropertiesDto
import com.example.minecraft.common.dto.BaseResponse
import com.example.minecraft.infra.server.ServerUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class FileService(
    val serverUtils: ServerUtils,
){

    fun getServerProperties(): BaseResponse<ServerPropertiesDto> {
        val file = File("/minecraft/server.properties")
        if (!file.exists()) throw IllegalArgumentException("file is not exists")

        val properties = serverUtils.getProperties(file)

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(), ServerPropertiesDto.of(properties), "")
    }
}