package com.example.mathrush.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameUiState(
    val currentQuestion: MathQuestion? = null,
    val score: Int = 0,
    val lives: Int = 3,
    val timeLeft: Int = 10, // 10 segundos por pregunta
    val maxTimePerQuestion: Int = 10,
    val streak: Int = 0,
    val isGameOver: Boolean = false,
    val difficulty: Int = 1,
    val userInput: String = ""
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startGame(difficultyLevel: Int = 1) {
        _uiState.value = GameUiState(
            difficulty = difficultyLevel,
            currentQuestion = QuestionGenerator.generateQuestion(difficultyLevel)
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val currentState = _uiState.value
                if (currentState.isGameOver) break

                if (currentState.timeLeft > 1) {
                    _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
                } else {
                    // ¡Se acabó el tiempo para esta pregunta! Cuenta como error / pierde vida
                    handleIncorrectAnswer()
                }
            }
        }
    }

    fun onNumberInput(digit: String) {
        if (_uiState.value.isGameOver) return
        _uiState.update { it.copy(userInput = it.userInput + digit) }
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

        val userAnswerInt = state.userInput.toIntOrNull() ?: return
        val correctAnswer = state.currentQuestion.correctAnswer

        if (userAnswerInt == correctAnswer) {
            handleCorrectAnswer()
        } else {
            handleIncorrectAnswer()
        }
    }

    private fun handleCorrectAnswer() {
        val state = _uiState.value
        val timeBonus = ScoreCalculator.calculateScore(state.timeLeft, state.maxTimePerQuestion, state.difficulty)
        val streakBonus = ScoreCalculator.calculateStreakBonus(state.streak + 1)
        val earnedPoints = timeBonus + streakBonus

        val newScore = state.score + earnedPoints
        val newStreak = state.streak + 1

        // Aumentar dificultad progresivamente cada 5 aciertos
        val newDifficulty = if (newStreak % 5 == 0) minOf(state.difficulty + 1, 5) else state.difficulty

        _uiState.update {
            it.copy(
                score = newScore,
                streak = newStreak,
                difficulty = newDifficulty,
                userInput = "",
                timeLeft = it.maxTimePerQuestion,
                currentQuestion = QuestionGenerator.generateQuestion(newDifficulty)
            )
        }
        startTimer() // Reiniciar temporizador para la siguiente pregunta
    }

    private fun handleIncorrectAnswer() {
        timerJob?.cancel()
        val state = _uiState.value
        val remainingLives = state.lives - 1

        if (remainingLives <= 0) {
            _uiState.update { it.copy(lives = 0, isGameOver = true, userInput = "") }
        } else {
            _uiState.update {
                it.copy(
                    lives = remainingLives,
                    streak = 0,
                    userInput = "",
                    timeLeft = it.maxTimePerQuestion,
                    currentQuestion = QuestionGenerator.generateQuestion(it.difficulty)
                )
            }
            startTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}