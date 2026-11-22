package com.example.mathrush.game

data class PracticeAnswerRecord(
    val question: MathQuestion,
    val userAnswer: String?,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int
)

data class CategoryPerformance(
    val category: MathCategory,
    var totalQuestions: Int = 0,
    var correctAnswers: Int = 0
) {
    val accuracy: Float
        get() = if (totalQuestions > 0) (correctAnswers.toFloat() / totalQuestions) * 100f else 0f
}

data class PracticeReport(
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val accuracyPercentage: Float,
    val averageTimeSeconds: Float,
    val categoryPerformances: Map<MathCategory, CategoryPerformance>,
    val weakestCategory: MathCategory?,
    val recommendations: List<String>
)

class PracticeSessionManager {
    private val records = mutableListOf<PracticeAnswerRecord>()

    fun recordAnswer(question: MathQuestion, userAnswer: String?, timeSpent: Int) {
        val isCorrect = userAnswer != null && question.correctAnswer.validate(userAnswer)
        records.add(PracticeAnswerRecord(question, userAnswer, isCorrect, timeSpent))
    }

    fun generateReport(): PracticeReport {
        val total = records.size
        if (total == 0) {
            return PracticeReport(0, 0, 0, 0f, 0f, emptyMap(), null, listOf("¡Realiza algunas prácticas para obtener recomendaciones!"))
        }

        val correct = records.count { it.isCorrect }
        val incorrect = total - correct
        val accuracy = (correct.toFloat() / total) * 100f
        val avgTime = records.map { it.timeSpentSeconds }.average().toFloat()

        val perfMap = mutableMapOf<MathCategory, CategoryPerformance>()
        for (record in records) {
            val cat = record.question.category
            val perf = perfMap.getOrPut(cat) { CategoryPerformance(cat) }
            perf.totalQuestions++
            if (record.isCorrect) {
                perf.correctAnswers++
            }
        }

        val weakest = perfMap.values.minByOrNull { it.accuracy }?.category
        val recommendations = mutableListOf<String>()

        if (weakest != null) {
            val weakPerf = perfMap[weakest]!!
            if (weakPerf.accuracy < 70f) {
                recommendations.add("Presentas dificultades en ${weakest.title} (${String.format("%.1f", weakPerf.accuracy)}% de precisión). Te recomendamos practicar más ejercicios de esta categoría.")
            } else {
                recommendations.add("¡Buen trabajo en general! Sigue practicando para perfeccionar todas las áreas matemáticas.")
            }
        }

        if (avgTime > 8f) {
            recommendations.add("Tu tiempo promedio de respuesta es alto (${String.format("%.1f", avgTime)}s). Intenta ganar mayor agilidad mental.")
        } else {
            recommendations.add("¡Excelente velocidad de respuesta!")
        }

        return PracticeReport(
            totalQuestions = total,
            correctCount = correct,
            incorrectCount = incorrect,
            accuracyPercentage = accuracy,
            averageTimeSeconds = avgTime,
            categoryPerformances = perfMap,
            weakestCategory = weakest,
            recommendations = recommendations
        )
    }

    fun reset() {
        records.clear()
    }
}
