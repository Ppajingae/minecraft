package com.example.minecraft.api.service

import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class ServerService {

    fun startServer(): BaseResponse<out String?> {
        return try {
            val processBuilder = ProcessBuilder("./bedrock_server")
            processBuilder.directory(File("/minecraft"))

            // 로그 파일 경로 명시 + 파일 생성
            val stdout = File("/minecraft/stdout.log")
            val stderr = File("/minecraft/stderr.log")
            stdout.createNewFile()
            stderr.createNewFile()

            processBuilder.redirectOutput(stdout)
            processBuilder.redirectError(stderr)

            val process = processBuilder.start()
            File("/minecraft/server.pid").writeText(process.pid().toString())

            BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(), "서버 실행됨", "")
        } catch (e: Exception) {
            e.printStackTrace()
            BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), null, "에러: ${e.message}")
        }
    }

    fun stopServer(): BaseResponse<Any>? {

        val pid = File("/minecraft/server.pid").readText().trim()

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

            File("/minecraft/server.pid").writeText(process.pid().toString())
        }else{
            val processBuilder = ProcessBuilder("./bedrock_server")
            val process = processBuilder.start()

            File("/minecraft/server.pid").writeText(process.pid().toString())
        }

        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now())
    }


}