package com.example.mathrush.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncManager(context: Context) {
    private val scoreRepository = ScoreRepository(context)
    private val authRepository = AuthRepository()
    private val firestoreRepository = FirestoreRepository()

    suspend fun syncPendingScores(): Boolean {
        val user = authRepository.currentUser ?: return false
        val unsynced = scoreRepository.getUnsyncedScores()
        if (unsynced.isEmpty()) return true

        return withContext(Dispatchers.IO) {
            var allSuccess = true
            for (score in unsynced) {
                val updatedScore = score.copy(userId = user.uid, username = user.displayName ?: user.email?.substringBefore("@") ?: "Jugador")
                val result = firestoreRepository.saveScore(updatedScore)
                if (result.isSuccess) {
                    scoreRepository.markSynced(score.gameId)
                } else {
                    allSuccess = false
                }
            }
            allSuccess
        }
    }
}
