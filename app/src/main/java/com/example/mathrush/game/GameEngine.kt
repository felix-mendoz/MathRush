package com.example.mathrush.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathrush.data.ScoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class GameUiState(
    val currentQuestion: MathQuestion? = null,
    val score: Int = 0,
    val lives: Int = 3,
    val timeLeft: Int = 20,
    val maxTimePerQuestion: Int = 20,
    val streak: Int = 0,
    val maxCombo: Int = 0,
    val comboMultiplier: Int = 1,
    val isGameOver: Boolean = false,
    val difficulty: Int = 1,
    val userInput: String = ""
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val scoreRepository = ScoreRepository(application)
    private val audioManager = AudioManager()

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startGame(difficultyLevel: Int = 1) {
        val diff = when (difficultyLevel) {
            1 -> MathDifficulty.EASY
            2 -> MathDifficulty.INTERMEDIATE
            else -> MathDifficulty.ADVANCED
        }
        _uiState.value = GameUiState(
            difficulty = difficultyLevel,
            maxTimePerQuestion = diff.defaultTimeSeconds,
            timeLeft = diff.defaultTimeSeconds,
            currentQuestion = QuestionGenerator.generateQuestion(null, diff)
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000.milliseconds)
                val currentState = _uiState.value
                if (currentState.isGameOver) break

                if (currentState.timeLeft > 1) {
                    _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
                } else {
                    handleIncorrectAnswer()
                }
            }
        }
    }

    fun onNumberInput(input: String) {
        if (_uiState.value.isGameOver) return
        _uiState.update { it.copy(userInput = it.userInput + input) }
    }

    fun onDeleteInput() {
        if (_uiState.value.isGameOver) return
        _uiState.update {
            if (it.userInput.isNotEmpty()) it.copy(userInput = it.userInput.dropLast(1))
            else it
        }
    }

    fun submitAnswer() {
        val state = _uiState.value
        if (state.isGameOver || state.currentQuestion == null) return

        val isCorrect = state.currentQuestion.correctAnswer.validate(state.userInput)

        if (isCorrect) {
            handleCorrectAnswer()
        } else {
            handleIncorrectAnswer()
        }
    }

    private fun handleCorrectAnswer() {
        val state = _uiState.value
        val newStreak = state.streak + 1
        val newMaxCombo = maxOf(state.maxCombo, newStreak)

        val multiplier = when {
            newStreak >= 10 -> 5
            newStreak >= 6 -> 3
            newStreak >= 3 -> 2
            else -> 1
        }

        if (multiplier > 1) {
            audioManager.playComboSound()
        } else {
            audioManager.playCorrectSound()
        }

        val timeBonus = ScoreCalculator.calculateScore(state.timeLeft, state.maxTimePerQuestion, state.difficulty)
        val streakBonus = ScoreCalculator.calculateStreakBonus(newStreak)
        val earnedPoints = (timeBonus + streakBonus) * multiplier

        val newScore = state.score + earnedPoints
        val newDifficulty = if (newStreak % 5 == 0) minOf(state.difficulty + 1, 5) else state.difficulty
        val diffEnum = when (newDifficulty) {
            1 -> MathDifficulty.EASY
            2 -> MathDifficulty.INTERMEDIATE
            else -> MathDifficulty.ADVANCED
        }

        _uiState.update {
            it.copy(
                score = newScore,
                streak = newStreak,
                maxCombo = newMaxCombo,
                comboMultiplier = multiplier,
                difficulty = newDifficulty,
                userInput = "",
                timeLeft = diffEnum.defaultTimeSeconds,
                maxTimePerQuestion = diffEnum.defaultTimeSeconds,
                currentQuestion = QuestionGenerator.generateQuestion(null, diffEnum)
            )
        }
        startTimer()
    }

    private fun handleIncorrectAnswer() {
        timerJob?.cancel()
        audioManager.playIncorrectSound()
        val state = _uiState.value
        val remainingLives = state.lives - 1
        val diffEnum = when (state.difficulty) {
            1 -> MathDifficulty.EASY
            2 -> MathDifficulty.INTERMEDIATE
            else -> MathDifficulty.ADVANCED
        }

        if (remainingLives <= 0) {
            audioManager.playGameOverSound()
            scoreRepository.saveScore(
                username = "Anthony966",
                score = state.score,
                maxCombo = state.maxCombo,
                difficulty = state.difficulty
            )
            _uiState.update { it.copy(lives = 0, isGameOver = true, userInput = "") }
        } else {
            _uiState.update {
                it.copy(
                    lives = remainingLives,
                    streak = 0,
                    comboMultiplier = 1,
                    userInput = "",
                    timeLeft = diffEnum.defaultTimeSeconds,
                    maxTimePerQuestion = diffEnum.defaultTimeSeconds,
                    currentQuestion = QuestionGenerator.generateQuestion(null, diffEnum)
                )
            }
            startTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        audioManager.release()
    }
}
