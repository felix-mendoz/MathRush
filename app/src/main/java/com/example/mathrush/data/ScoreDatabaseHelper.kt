package com.example.mathrush.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.UUID

class ScoreDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mathrush_scores.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_SCORES = "scores"

        private const val COLUMN_ID = "id"
        private const val COLUMN_GAME_ID = "game_id"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_SCORE = "score"
        private const val COLUMN_MAX_COMBO = "max_combo"
        private const val COLUMN_DIFFICULTY = "difficulty"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_SUBCATEGORY = "subcategory"
        private const val COLUMN_IS_SYNCED = "is_synced"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_SCORES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_GAME_ID TEXT UNIQUE, "
                + "$COLUMN_USER_ID TEXT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_SCORE INTEGER, "
                + "$COLUMN_MAX_COMBO INTEGER, "
                + "$COLUMN_DIFFICULTY INTEGER, "
                + "$COLUMN_CATEGORY TEXT, "
                + "$COLUMN_SUBCATEGORY TEXT, "
                + "$COLUMN_IS_SYNCED INTEGER, "
                + "$COLUMN_TIMESTAMP INTEGER)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_SCORES")
            onCreate(db)
        }
    }

    fun insertScore(scoreEntity: ScoreEntity): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GAME_ID, scoreEntity.gameId.ifEmpty { UUID.randomUUID().toString() })
            put(COLUMN_USER_ID, scoreEntity.userId)
            put(COLUMN_USERNAME, scoreEntity.username)
            put(COLUMN_SCORE, scoreEntity.score)
            put(COLUMN_MAX_COMBO, scoreEntity.maxCombo)
            put(COLUMN_DIFFICULTY, scoreEntity.difficulty)
            put(COLUMN_CATEGORY, scoreEntity.category)
            put(COLUMN_SUBCATEGORY, scoreEntity.subcategory)
            put(COLUMN_IS_SYNCED, if (scoreEntity.isSynced) 1 else 0)
            put(COLUMN_TIMESTAMP, scoreEntity.timestamp)
        }
        val id = db.insertWithOnConflict(TABLE_SCORES, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return id
    }

    fun getUnsyncedScores(): List<ScoreEntity> {
        val scores = mutableListOf<ScoreEntity>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SCORES,
            null,
            "$COLUMN_IS_SYNCED = ?",
            arrayOf("0"),
            null,
            null,
            "$COLUMN_TIMESTAMP ASC"
        )
        cursor.use {
            val idCol = it.getColumnIndex(COLUMN_ID)
            val gameIdCol = it.getColumnIndex(COLUMN_GAME_ID)
            val userIdCol = it.getColumnIndex(COLUMN_USER_ID)
            val userCol = it.getColumnIndex(COLUMN_USERNAME)
            val scoreCol = it.getColumnIndex(COLUMN_SCORE)
            val comboCol = it.getColumnIndex(COLUMN_MAX_COMBO)
            val diffCol = it.getColumnIndex(COLUMN_DIFFICULTY)
            val catCol = it.getColumnIndex(COLUMN_CATEGORY)
            val subCol = it.getColumnIndex(COLUMN_SUBCATEGORY)
            val syncedCol = it.getColumnIndex(COLUMN_IS_SYNCED)
            val timeCol = it.getColumnIndex(COLUMN_TIMESTAMP)

            while (it.moveToNext()) {
                scores.add(
                    ScoreEntity(
                        id = if (idCol >= 0) it.getLong(idCol) else 0L,
                        gameId = if (gameIdCol >= 0) it.getString(gameIdCol) ?: UUID.randomUUID().toString() else UUID.randomUUID().toString(),
                        userId = if (userIdCol >= 0) it.getString(userIdCol) ?: "" else "",
                        username = if (userCol >= 0) it.getString(userCol) ?: "Jugador" else "Jugador",
                        score = if (scoreCol >= 0) it.getInt(scoreCol) else 0,
                        maxCombo = if (comboCol >= 0) it.getInt(comboCol) else 0,
                        difficulty = if (diffCol >= 0) it.getInt(diffCol) else 1,
                        category = if (catCol >= 0) it.getString(catCol) ?: "Aritmética" else "Aritmética",
                        subcategory = if (subCol >= 0) it.getString(subCol) ?: "Operaciones básicas" else "Operaciones básicas",
                        isSynced = if (syncedCol >= 0) it.getInt(syncedCol) == 1 else false,
                        timestamp = if (timeCol >= 0) it.getLong(timeCol) else System.currentTimeMillis()
                    )
                )
            }
        }
        db.close()
        return scores
    }

    fun markScoreAsSynced(gameId: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_SYNCED, 1)
        }
        db.update(TABLE_SCORES, values, "$COLUMN_GAME_ID = ?", arrayOf(gameId))
        db.close()
    }

    fun getAllScores(): List<ScoreEntity> {
        val scores = mutableListOf<ScoreEntity>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SCORES,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_SCORE DESC, $COLUMN_TIMESTAMP DESC"
        )

        cursor.use {
            val idCol = it.getColumnIndex(COLUMN_ID)
            val gameIdCol = it.getColumnIndex(COLUMN_GAME_ID)
            val userIdCol = it.getColumnIndex(COLUMN_USER_ID)
            val userCol = it.getColumnIndex(COLUMN_USERNAME)
            val scoreCol = it.getColumnIndex(COLUMN_SCORE)
            val comboCol = it.getColumnIndex(COLUMN_MAX_COMBO)
            val diffCol = it.getColumnIndex(COLUMN_DIFFICULTY)
            val catCol = it.getColumnIndex(COLUMN_CATEGORY)
            val subCol = it.getColumnIndex(COLUMN_SUBCATEGORY)
            val syncedCol = it.getColumnIndex(COLUMN_IS_SYNCED)
            val timeCol = it.getColumnIndex(COLUMN_TIMESTAMP)

            while (it.moveToNext()) {
                val scoreEntity = ScoreEntity(
                    id = if (idCol >= 0) it.getLong(idCol) else 0L,
                    gameId = if (gameIdCol >= 0) it.getString(gameIdCol) ?: UUID.randomUUID().toString() else UUID.randomUUID().toString(),
                    userId = if (userIdCol >= 0) it.getString(userIdCol) ?: "" else "",
                    username = if (userCol >= 0) it.getString(userCol) else "Jugador",
                    score = if (scoreCol >= 0) it.getInt(scoreCol) else 0,
                    maxCombo = if (comboCol >= 0) it.getInt(comboCol) else 0,
                    difficulty = if (diffCol >= 0) it.getInt(diffCol) else 1,
                    category = if (catCol >= 0) it.getString(catCol) ?: "Aritmética" else "Aritmética",
                    subcategory = if (subCol >= 0) it.getString(subCol) ?: "Operaciones básicas" else "Operaciones básicas",
                    isSynced = if (syncedCol >= 0) it.getInt(syncedCol) == 1 else false,
                    timestamp = if (timeCol >= 0) it.getLong(timeCol) else System.currentTimeMillis()
                )
                scores.add(scoreEntity)
            }
        }
        db.close()
        return scores
    }
}
