package com.example.minecraft.infra.server

import org.springframework.stereotype.Component
import java.io.File

@Component
class ServerUtils {

    // 서버에 파일이 필요할 경우
    fun createServerFile(){
        TODO()
    }

    // 서버에 파일이 필요할 경우
    fun updateProperties(path: String, prefix: String, description: String){
        val propsFile = File(path)
        val lines = propsFile.readLines()
        val updatedLines = lines.map { line ->
            if (line.startsWith(prefix)) "level-name=$description" else line
        }
        propsFile.writeText(updatedLines.joinToString("\n"))
    }

    //서버에서 디렉터리를 삭제하는 경우
    fun prepareCleanDir(dir: File){
        if(dir.exists()) dir.deleteRecursively()
        if(!dir.mkdirs()){
            throw IllegalArgumentException("디렉토리 생성 실패: ${dir.absolutePath}")
        }
    }

    //서버에서 디렉터리를 초기화하는 경우
    fun prepareParentForFile(file: File) {
        val parent = file.parentFile
        if (parent != null && !parent.exists()) {
            parent.mkdirs()
        }
    }

    fun renameParent(file: File, newFile: File){
        if (!file.renameTo(newFile)) {
            throw IllegalStateException("월드 디렉토리 이름 변경 실패")
        }
    }

}