package com.example.minecraft.api.controller

import com.example.minecraft.api.service.EtcApiService
import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/api/v1")
class EtcApiController(
    val etcApiService: EtcApiService
){

    @PostMapping("/server/upload")
    fun uploadWorld(
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<BaseResponse<out String?>>{

        return ResponseEntity.status(HttpStatus.OK).body(etcApiService.uploadWorld(file))
    }

    @GetMapping("/server/download")
    fun downloadWorld(){

    }

}