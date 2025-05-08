package com.example.minecraft.api.service

import com.example.minecraft.common.dto.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.zip.ZipInputStream

@Service
class EtcApiService {

    fun uploadWorld(file: MultipartFile): BaseResponse<String?> {
        if (file.isEmpty) throw IllegalArgumentException("file is empty")

        val originalFileName = file.originalFilename ?: "uploaded_world.zip"

        // 업로드 zip 저장
        val uploadDir = File("/minecraft/uploads")
        uploadDir.mkdirs()
        val safeFileName = file.originalFilename ?: "uploaded_world.zip"
        if (!safeFileName.endsWith(".zip")) {
            throw IllegalArgumentException("지원하지 않는 파일 형식입니다. (.zip만 허용)")
        }
        val uploadedFile = File(uploadDir, safeFileName)
        file.transferTo(uploadedFile)

        // 기존 월드 삭제

        val worldDir = File("/minecraft/worlds")
        if (worldDir.exists()) {
            worldDir.deleteRecursively()
        }
        worldDir.mkdirs()

        //최상위 디렉터리 추출
        val topLevelDir = ZipInputStream(uploadedFile.inputStream()).use { zip ->
            val topLevels = mutableSetOf<String>()

            var entry = zip.nextEntry
            while (entry != null) {
                val entryName = entry.name.trimStart('/')
                val top = entryName.split("/").firstOrNull()
                if (!top.isNullOrBlank()) {
                    topLevels.add(top)
                }
                entry = zip.nextEntry
            }

            if (topLevels.size != 1) {
                throw IllegalArgumentException("월드 압축파일은 하나의 최상위 폴더만 포함해야 합니다.")
            }

            topLevels.first()
        }.removeSurrounding("'").trim()

        val levelName = originalFileName.removeSuffix(".zip").trim()
        val worldBaseDir = File("/minecraft/worlds")
        val extractedDir = File(worldBaseDir, topLevelDir)
        val finalDir = File(worldBaseDir, levelName)

        //properties 수정
        val propsFile = File("/minecraft/server.properties")
        val lines = propsFile.readLines()
        val updatedLines = lines.map { line ->
            if (line.startsWith("level-name=")) "level-name=$levelName" else line
        }
        propsFile.writeText(updatedLines.joinToString("\n"))



        // 기존 삭제 및 압축 해제
        if (finalDir.exists()) finalDir.deleteRecursively()
        if (extractedDir.exists()) extractedDir.deleteRecursively()
        worldBaseDir.mkdirs()

        ZipInputStream(uploadedFile.inputStream()).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                val outFile = File(worldBaseDir, entry.name).normalize()
                if (!outFile.toPath().startsWith(worldBaseDir.toPath())) {
                    throw IllegalArgumentException("압축에 이상한 경로 포함")
                }

                if (entry.isDirectory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile.mkdirs()
                    outFile.outputStream().use { zip.copyTo(it) }
                }
                entry = zip.nextEntry
            }
        }

        // 디렉토리 이름 변경
        if (!extractedDir.renameTo(finalDir)) {
            throw IllegalStateException("월드 디렉토리 이름 변경 실패")
        }


        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(),"월드 업로드 및 적용 완료")
    }
}