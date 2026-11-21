package com.example.mathrush.data

import android.content.Context

class ScoreRepository(context: Context) {
    private val dbHelper = ScoreDatabaseHelper(context)

    fun saveScore(
        username: String,
        score: Int,
        maxCombo: Int,
        difficulty: Int,
        category: String = "Aritmética",
        subcategory: String = "Operaciones básicas",
        userId: String = "",
        isSynced: Boolean = false
    ): ScoreEntity {
        val entity = ScoreEntity(
            username = username.ifEmpty { "Jugador" },
            score = score,
            maxCombo = maxCombo,
            difficulty = difficulty,
            category = category,
            subcategory = subcategory,
            userId = userId,
            isSynced = isSynced
        )
        dbHelper.insertScore(entity)
        return entity
    }

    fun getScores(): List<ScoreEntity> {
        return dbHelper.getAllScores()
    }

    fun getUnsyncedScores(): List<ScoreEntity> {
        return dbHelper.getUnsyncedScores()
    }

    fun markSynced(gameId: String) {
        dbHelper.markScoreAsSynced(gameId)
    }
}
