package com.example.minecraft

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MinecraftApplication

fun main(args: Array<String>) {
    runApplication<MinecraftApplication>(*args)
}
