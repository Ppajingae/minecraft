package com.example.minecraft.api.controller

import com.example.minecraft.api.service.ServerService
import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ServerController(
    private val serverService: ServerService
){


    @PostMapping("/server/start")
    fun startServer(): ResponseEntity<BaseResponse<out String?>>
        = ResponseEntity.status(HttpStatus.OK).body(serverService.startServer())

    @PostMapping("/server/stop")
    fun stopServer(): ResponseEntity<BaseResponse<Any>>
        = ResponseEntity.status(HttpStatus.OK).body(serverService.stopServer())

    @GetMapping("/server/status")
    fun statusServer(): ResponseEntity<BaseResponse<Any>>
        = ResponseEntity.status(HttpStatus.OK).body(serverService.statusServer())

    @PostMapping("/server/restart")
    fun restartServer(): ResponseEntity<BaseResponse<Any>>
        = ResponseEntity.status(HttpStatus.OK).body(serverService.restartServer())

}