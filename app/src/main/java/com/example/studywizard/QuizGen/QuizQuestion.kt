package com.example.studywizard.QuizGen

data class QuizQuestion(
    val text: String,
    val choices: Map<String, String>,
    val correctAnswer: String
)

fun parseQuizOutput(quiz: String): List<QuizQuestion> {
    val result = mutableListOf<QuizQuestion>()
    val blocks = quiz.trim().split(Regex("""(?=\d+\.\s)""")) // Splits at 1. , 2. , 3. etc.

    for (block in blocks) {
        val lines = block.trim().lines().map { it.trim() }.filter { it.isNotBlank() }

        if (lines.size < 6) continue

        val text = lines[0].removePrefix(Regex("""\d+\.\s*""").toString()).trim()

        val choices = mutableMapOf<String, String>()
        for (line in lines.drop(1)) {
            val match = Regex("""^([A-Da-d])[.)]\s+(.*)""").find(line)
            if (match != null) {
                choices[match.groupValues[1].uppercase()] = match.groupValues[2].trim()
            }
        }

        val answerLine = lines.find { it.contains("Answer", ignoreCase = true) }
        val answer = answerLine?.substringAfter(":")?.trim()?.uppercase()

        if (text.isNotBlank() && choices.size >= 3 && answer in choices.keys) {
            result.add(QuizQuestion(text, choices, answer!!))
        }
    }

    return result
}
