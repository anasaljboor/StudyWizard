package com.example.studywizard.Cohere_ML



fun buildSummaryPrompt(text: String) = "Summarize the following notes:\n$text"

fun buildFlashcardsPrompt(text: String) = "Create 3 flashcards based on the following:\n$text"

fun buildQuizPrompt(text: String) = "Generate 3 quiz questions from this content:\n$text"
