package com.example.mathrush.game

object ScoreCalculator {

    // Calcula los puntos otorgados por una respuesta correcta incluyendo multiplicador por dificultad
    fun calculateScore(timeRemainingSeconds: Int, maxTimeSeconds: Int, difficultyLevel: Int): Int {
        val difficultyMultiplier = when (difficultyLevel) {
            1 -> 1.0 // Fácil
            2 -> 1.5 // Intermedio
            else -> 2.0 // Avanzado
        }
        val basePoints = (10 * difficultyLevel).toDouble()
        val timeBonus = (timeRemainingSeconds * 2.0) * difficultyLevel
        return ((basePoints + timeBonus) * difficultyMultiplier).toInt()
    }

    // Bono por racha (streak)
    fun calculateStreakBonus(currentStreak: Int): Int {
        return if (currentStreak >= 3) currentStreak * 5 else 0
    }
}
