package com.example.studywizard.QuizGen

data class QuizQuestion(
    val text: String,
    val choices: Map<String, String>,
    val correctAnswer: String
)

fun parseQuizOutput(quiz: String): List<QuizQuestion> {
    val questions = quiz.trim().split(Regex("\\n\\s*\\d+\\.")).filter { it.isNotBlank() }
    val parsed = mutableListOf<QuizQuestion>()

    for (q in questions) {
        val lines = q.trim().lines()
        if (lines.size < 3) continue

        val text = lines[0].trim()
        val choices = mutableMapOf<String, String>()
        var answer = ""

        lines.drop(1).forEach { line ->
            val match = Regex("^([A-D])\\.\\s*(.*)").find(line)
            if (match != null) {
                choices[match.groupValues[1]] = match.groupValues[2]
            } else if (line.startsWith("Answer:", true)) {
                answer = line.substringAfter(":").trim()
            }
        }

        if (text.isNotBlank() && choices.isNotEmpty() && answer.isNotBlank()) {
            parsed.add(QuizQuestion(text, choices, answer))
        }
    }

    return parsed
}
