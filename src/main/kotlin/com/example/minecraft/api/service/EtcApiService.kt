package com.example.minecraft.api.service

import com.example.minecraft.common.dto.BaseResponse
import com.example.minecraft.infra.server.ServerUtils
import com.example.minecraft.infra.zip.ZipUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime

@Service
class EtcApiService(
    private val zipUtils: ZipUtils,
    private val serverUtils: ServerUtils,
){

    fun uploadWorld(file: MultipartFile): BaseResponse<String?> {
        if (file.isEmpty) throw IllegalArgumentException("file is empty")

        // 업로드 zip 저장
        val safeFileName = file.originalFilename ?: "upload_file.zip"
        val uploadedFile = zipUtils.savedZipFile("/minecraft/uploads", safeFileName, file)

        // 기존 월드 삭제

        val worldDir = File("/minecraft/worlds")
        serverUtils.prepareCleanDir(worldDir)
        serverUtils.prepareParentForFile(worldDir)

        //최상위 디렉터리 추출
        val topLevelDir = zipUtils.getTopLevelDirectory(uploadedFile)

        val levelName = safeFileName.removeSuffix(".zip").trim()
        val worldBaseDir = File("/minecraft/worlds")
        val extractedDir = File(worldBaseDir, topLevelDir)
        val finalDir = File(worldBaseDir, levelName)

        //properties 수정
        serverUtils.updateProperties("/minecraft/server.properties", "level-name=", levelName)

        // 기존 삭제
        serverUtils.prepareCleanDir(extractedDir)
        serverUtils.prepareCleanDir(finalDir)
        serverUtils.prepareParentForFile(finalDir)

        //압축 해제
        zipUtils.unzipFile(uploadedFile, worldBaseDir)

        // 디렉토리 이름 변경
        serverUtils.renameParent(extractedDir, finalDir)


        return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now(),"월드 업로드 및 적용 완료")
    }
}