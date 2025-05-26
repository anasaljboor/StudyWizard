package com.example.studywizard.Cohere_ML

data class GenerateRequest(
    val model: String = "command", // optional based on Cohere version
    val prompt: String,
    val max_tokens: Int = 100,
    val temperature: Double = 0.7,
    val k: Int = 0,
    val stop_sequences: List<String> = listOf(),
    val return_likelihoods: String = "NONE"
)

data class GenerateResponse(
    val generations: List<Generation>
)

data class Generation(
    val text: String
)
