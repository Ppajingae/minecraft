package com.example.minecraft.infra.zip

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.zip.ZipInputStream

@Component
class ZipUtils {


    // 최상위 디렉터리 이름 호출
    fun getTopLevelDirectory(uploadedFile: File): String{
        return ZipInputStream(uploadedFile.inputStream()).use { zip ->
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
    }

    fun savedZipFile(path: String, fileName: String, file: MultipartFile): File{
        val uploadDir = File(path)
        uploadDir.mkdirs()
        if (!fileName.endsWith(".zip")) {
            throw IllegalArgumentException("지원하지 않는 파일 형식입니다. (.zip만 허용)")
        }
        val uploadedFile = File(uploadDir, fileName)
        file.transferTo(uploadedFile)

        return uploadedFile
    }


    fun unzipFile(uploadedFile: File, worldBaseDir: File){
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
    }
}