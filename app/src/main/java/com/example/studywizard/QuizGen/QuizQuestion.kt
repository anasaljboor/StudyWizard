package com.example.studywizard.QuizGen

data class QuizQuestion(
    val text: String,
    val choices: Map<String, String>,
    val correctAnswer: String
)

fun parseQuizOutput(quiz: String): List<QuizQuestion> {
    val questionBlocks = quiz.trim().split(Regex("\\n(?=\\d+\\.)")) // split on new questions like "1.", "2." etc.
    val result = mutableListOf<QuizQuestion>()

    for (block in questionBlocks) {
        val lines = block.lines().map { it.trim() }.filter { it.isNotEmpty() }

        val questionLine = lines.firstOrNull() ?: continue
        val questionText = questionLine.substringAfter(".").trim()

        val choices = mutableMapOf<String, String>()
        var answer: String? = null

        for (line in lines.drop(1)) {
            when {
                line.startsWith("A", ignoreCase = true) -> choices["A"] = line.removePrefix("A.").removePrefix("A)").trim()
                line.startsWith("B", ignoreCase = true) -> choices["B"] = line.removePrefix("B.").removePrefix("B)").trim()
                line.startsWith("C", ignoreCase = true) -> choices["C"] = line.removePrefix("C.").removePrefix("C)").trim()
                line.startsWith("D", ignoreCase = true) -> choices["D"] = line.removePrefix("D.").removePrefix("D)").trim()
                line.contains("Answer", ignoreCase = true) -> answer = line.substringAfter(":").substringAfter(".").trim().uppercase()
            }
        }

        if (questionText.isNotBlank() && choices.size == 4 && !answer.isNullOrBlank()) {
            result.add(QuizQuestion(text = questionText, choices = choices, correctAnswer = answer!!))
        }
    }

    return result
}
