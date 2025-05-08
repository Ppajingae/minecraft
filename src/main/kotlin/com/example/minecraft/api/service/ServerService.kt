package com.example.minecraft.api.service

import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class ServerService {

    fun startServer(): BaseResponse<Any>? {
        val processBuilder = ProcessBuilder("./bedrock_server")
            processBuilder.directory(File("/home/kimdoguyn/minecraft"))
            processBuilder.redirectOutput(File("stdout 로그 파일"))
            processBuilder.redirectError(File("stderr 로그 파일"))
        val process = processBuilder.start()

            File("/home/kimdoguyn/minecraft/server.pid").writeText(process.pid().toString())

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(),)
    }

    fun stopServer(): BaseResponse<Any>? {

        val pid = File("/home/kimdoguyn/minecraft/server.pid").readText().trim()

        ProcessBuilder("kill", pid).start()

        val pidProcess = ProcessBuilder("pgrep", "-f", "bedrock_server").start()
        val pidText = pidProcess.inputStream.bufferedReader().readText().trim()

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(), "실행 중인 서버 : $pidText")
    }

    fun statusServer(): BaseResponse<Any>? {
        val pidProcess = ProcessBuilder("pgrep", "-f", "bedrock_server").start()
        val pidText = pidProcess.inputStream.bufferedReader().readText().trim()

        val status = if (pidText.isNotEmpty()) "$pidText server is running" else "$pidText server is stopped"

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(), status)
    }

    fun restartServer(): BaseResponse<Any>? {
        val pidProcess = ProcessBuilder("pgrep", "-f", "bedrock_server").start()
        val pidText = pidProcess.inputStream.bufferedReader().readText().trim()

        if (pidText.isNotEmpty()) {
            ProcessBuilder("kill", pidText).start()
            val processBuilder = ProcessBuilder("./bedrock_server")
            val process = processBuilder.start()

            File("/home/kimdoguyn/minecraft/server.pid").writeText(process.pid().toString())
        }else{
            val processBuilder = ProcessBuilder("./bedrock_server")
            val process = processBuilder.start()

            File("/home/kimdoguyn/minecraft/server.pid").writeText(process.pid().toString())
        }

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now())
    }


}