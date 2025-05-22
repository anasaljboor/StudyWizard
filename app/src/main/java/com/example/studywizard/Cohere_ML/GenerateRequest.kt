package com.example.studywizard.Cohere_ML

data class GenerateRequest(val model: String = "command",
                           val prompt: String,
                           val max_tokens: Int = 300,
                           val temperature: Double = 0.7)
data class GenerateResponse(
    val generations: List<Generation>
)

data class Generation(
    val text: String
)
