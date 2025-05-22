package com.example.studywizard.Cohere_ML

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CohereApi {
    @POST("generate")
    @Headers("Content-Type: application/json")
    suspend fun generate(
        @Body request: GenerateRequest,
        @Header("Authorization") auth: String
    ): Response<GenerateResponse>
}