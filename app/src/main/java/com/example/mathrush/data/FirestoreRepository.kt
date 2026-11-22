package com.example.mathrush.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

data class LeaderboardItem(
    val gameId: String = "",
    val userId: String = "",
    val username: String = "",
    val score: Int = 0,
    val maxCombo: Int = 0,
    val difficulty: Int = 1,
    val category: String = "",
    val timestamp: Long = 0L
)

class FirestoreRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val scoresCollection = firestore.collection("scores")

    suspend fun saveScore(scoreEntity: ScoreEntity): Result<Unit> {
        return try {
            val docId = scoreEntity.gameId.ifEmpty { java.util.UUID.randomUUID().toString() }
            val data = mapOf(
                "gameId" to docId,
                "userId" to scoreEntity.userId,
                "username" to scoreEntity.username,
                "score" to scoreEntity.score,
                "maxCombo" to scoreEntity.maxCombo,
                "difficulty" to scoreEntity.difficulty,
                "category" to scoreEntity.category,
                "subcategory" to scoreEntity.subcategory,
                "timestamp" to scoreEntity.timestamp
            )
            scoresCollection.document(docId).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGlobalLeaderboard(limit: Long = 50): Result<List<LeaderboardItem>> {
        return try {
            val snapshot = scoresCollection
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val items = snapshot.documents.mapNotNull { doc ->
                LeaderboardItem(
                    gameId = doc.getString("gameId") ?: "",
                    userId = doc.getString("userId") ?: "",
                    username = doc.getString("username") ?: "Jugador",
                    score = doc.getLong("score")?.toInt() ?: 0,
                    maxCombo = doc.getLong("maxCombo")?.toInt() ?: 0,
                    difficulty = doc.getLong("difficulty")?.toInt() ?: 1,
                    category = doc.getString("category") ?: "Aritmética",
                    timestamp = doc.getLong("timestamp") ?: 0L
                )
            }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
