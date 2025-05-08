package com.example.minecraft.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class EtcApiController {

    @PostMapping("/server/upload")
    fun uploadWorld(){

    }

    @GetMapping("/server/download")
    fun downloadWorld(){

    }

}