package com.example.studywizard.QuizGen

data class QuizQuestion(
    val text: String,
    val choices: Map<String, String>,
    val correctAnswer: String
)

fun parseQuizOutput(quiz: String): List<QuizQuestion> {
    val result = mutableListOf<QuizQuestion>()
    val blocks = quiz.trim().split(Regex("\\n(?=\\d+\\.)"))  // Split at each new question

    for (block in blocks) {
        val lines = block.trim().lines()
        if (lines.size < 5) continue

        val questionText = lines[0].substringAfter(".").trim()
        val choices = mutableMapOf<String, String>()

        lines.drop(1).forEach { line ->
            val match = Regex("^([A-Da-d])\\.?\\)?\\s+(.*)").find(line.trim())
            if (match != null) {
                choices[match.groupValues[1].uppercase()] = match.groupValues[2].trim()
            }
        }

        val answerLine = lines.lastOrNull { it.contains("Answer", ignoreCase = true) }
        val correct = answerLine
            ?.substringAfter(":")
            ?.trim()
            ?.uppercase()
            ?.takeIf { it in choices.keys }

        if (questionText.isNotBlank() && choices.size >= 2 && correct != null) {
            result.add(QuizQuestion(questionText, choices, correct))
        } else {
            println("⚠️ Skipping question due to missing or invalid answer: \"$questionText\"")
        }
    }

    return result
}
