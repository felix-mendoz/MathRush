package com.example.mathrush.game

object ScoreCalculator {

    // Calcula los puntos otorgados por una respuesta correcta
    fun calculateScore(timeRemainingSeconds: Int, maxTimeSeconds: Int, difficultyLevel: Int): Int {
        val basePoints = 10 * difficultyLevel
        // Bono proporcional al tiempo que sobró
        val timeBonus = (timeRemainingSeconds * 2) * difficultyLevel
        return basePoints + timeBonus
    }

    // Penalización opcional o cálculo de racha (streak)
    fun calculateStreakBonus(currentStreak: Int): Int {
        return if (currentStreak >= 3) currentStreak * 5 else 0
    }
}