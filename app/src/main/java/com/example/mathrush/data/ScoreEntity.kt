package com.example.mathrush.data

import java.util.UUID

data class ScoreEntity(
    val id: Long = 0,
    val gameId: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val username: String,
    val score: Int,
    val maxCombo: Int,
    val difficulty: Int,
    val category: String = "Aritmética",
    val subcategory: String = "Operaciones básicas",
    val isSynced: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
