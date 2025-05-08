package com.example.minecraft.common.dto

import java.time.LocalDateTime

/**
 * <p>공통 응답 형식.
 * <p>예)</p>
 * <code>
 *     {
 *         "statusCode": 200,
 *         "timestamp": "2025-03-09H23:00:00.222",
 *         "content": {
 *             "memberId": 200,
 *             "name": "GDP"
 *         },
 *         "message": "Hello, World!"
 *     }
 * </code>
 *
 * @param <T> 응답 처리 결과의 타입.
 * @author Ppajingae
 */

data class BaseResponse<T>(
    val statusCode: Int,
    val timestamp: LocalDateTime,
    val content: Any?,
    val message: String?
){

    companion object {
        fun <T> of(statusCode: Int, timestamp: LocalDateTime, content: T, message: String): BaseResponse<T> {
            return BaseResponse<T>(statusCode, timestamp, content, message)
        }

        fun <T> of(statusCode: Int, timestamp: LocalDateTime, message: String): BaseResponse<T>{
            return BaseResponse<T>(statusCode, timestamp, null, message)
        }

        fun <T> of(statusCode: Int, timestamp: LocalDateTime): BaseResponse<T>{
            return BaseResponse<T>(statusCode, timestamp, null, null)
        }

        fun <T> of(statusCode: Int): BaseResponse<T>{
            return BaseResponse<T>(statusCode, LocalDateTime.now(), null, null)
        }

        fun <T> of(statusCode: Int, content: T): BaseResponse<T>{
            return BaseResponse<T>(statusCode, LocalDateTime.now(), content, null)
        }

        fun <T> of(statusCode: Int, content: T, message: String): BaseResponse<T>{
            return BaseResponse<T>(statusCode, LocalDateTime.now(), content, message)
        }
    }
}