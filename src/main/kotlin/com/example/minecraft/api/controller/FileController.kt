package com.example.minecraft.api.controller

import com.example.minecraft.api.dto.ServerPropertiesDto
import com.example.minecraft.api.service.FileService
import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class FileController(
    val fileService: FileService
){

    @PostMapping("/server/backup")
    fun backupServer(){

    }

    @GetMapping("/server/list")
    fun getBackupServer(){

    }

    @GetMapping("/server/logs")
    fun getServerLog(){

    }

    @GetMapping("server/properties")
    fun getServerProperties(): ResponseEntity<BaseResponse<ServerPropertiesDto>>
        = ResponseEntity.status(HttpStatus.OK).body(fileService.getServerProperties())
}