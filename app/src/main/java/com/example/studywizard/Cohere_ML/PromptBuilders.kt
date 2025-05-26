package com.example.studywizard.Cohere_ML



fun buildSummaryPrompt(topic: String, text: String) =
    "You are a tutor. Summarize the key points of the following notes on '$topic':\n$text"

fun buildFlashcardsPrompt(topic: String, text: String) =
    "Generate 3 flashcards about '$topic'. Format them as 'Q: ... A: ...' based on:\n$text"


fun buildQuizPrompt(topic: String, text: String) =
    "Generate 3 multiple-choice quiz questions on '$topic'. Each question should have 4 options and the correct answer marked."

