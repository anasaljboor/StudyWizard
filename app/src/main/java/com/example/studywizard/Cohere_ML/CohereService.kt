package com.example.studywizard.Cohere_ML

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CohereService {
    val api: CohereApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.cohere.ai/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CohereApi::class.java)
    }
}
