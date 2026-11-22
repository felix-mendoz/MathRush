package com.example.mathrush.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathrush.data.AuthRepository
import com.example.mathrush.data.ScoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

sealed interface GameEvent {
    data class ShowResults(
        val score: Int,
        val report: PracticeReport?
    ) : GameEvent
}

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
    val userInput: String = "",
    val isPracticeMode: Boolean = false,
    val practiceQuestionsAnswered: Int = 0,
    val practiceReport: PracticeReport? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val scoreRepository = ScoreRepository(application)
    private val audioManager = AudioManager()
    private val practiceSessionManager = PracticeSessionManager()

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var timerJob: Job? = null
    private var questionStartTime: Long = 0
    private var hasFinished = false

    fun startGame(difficultyLevel: Int = 1, practiceMode: Boolean = false) {
        val diff = when (difficultyLevel) {
            1 -> MathDifficulty.EASY
            2 -> MathDifficulty.INTERMEDIATE
            else -> MathDifficulty.ADVANCED
        }
        timerJob?.cancel()
        hasFinished = false
        practiceSessionManager.reset()
        questionStartTime = System.currentTimeMillis()

        _uiState.value = GameUiState(
            difficulty = difficultyLevel,
            maxTimePerQuestion = diff.defaultTimeSeconds,
            timeLeft = diff.defaultTimeSeconds,
            lives = 3,
            score = 0,
            streak = 0,
            maxCombo = 0,
            comboMultiplier = 1,
            isGameOver = false,
            userInput = "",
            isPracticeMode = practiceMode,
            practiceQuestionsAnswered = 0,
            practiceReport = null,
            currentQuestion = QuestionGenerator.generateQuestion(null, diff)
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        questionStartTime = System.currentTimeMillis()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000.milliseconds)
                val currentState = _uiState.value
                if (currentState.isGameOver || hasFinished) break

                if (currentState.timeLeft > 1) {
                    _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
                } else {
                    if (currentState.isPracticeMode) {
                        val q = currentState.currentQuestion
                        if (q != null) {
                            val timeSpent = ((System.currentTimeMillis() - questionStartTime) / 1000).toInt()
                            practiceSessionManager.recordAnswer(q, null, timeSpent)
                        }
                        advancePracticeOrFinish()
                    } else {
                        handleIncorrectAnswer()
                    }
                }
            }
        }
    }

    fun onNumberInput(input: String) {
        if (_uiState.value.isGameOver || hasFinished) return
        _uiState.update { it.copy(userInput = it.userInput + input) }
    }

    fun onDeleteInput() {
        if (_uiState.value.isGameOver || hasFinished) return
        _uiState.update {
            if (it.userInput.isNotEmpty()) it.copy(userInput = it.userInput.dropLast(1))
            else it
        }
    }

    fun submitAnswer() {
        val state = _uiState.value
        if (state.isGameOver || hasFinished || state.currentQuestion == null) return

        val userAnswer = state.userInput
        val q = state.currentQuestion
        val isCorrect = q.correctAnswer.validate(userAnswer)
        val timeSpent = ((System.currentTimeMillis() - questionStartTime) / 1000).toInt()

        if (state.isPracticeMode) {
            practiceSessionManager.recordAnswer(q, userAnswer.ifEmpty { null }, timeSpent)
            if (isCorrect) {
                audioManager.playCorrectSound()
            } else {
                audioManager.playIncorrectSound()
            }
            advancePracticeOrFinish()
        } else {
            if (isCorrect) {
                handleCorrectAnswer()
            } else {
                handleIncorrectAnswer()
            }
        }
    }

    private fun finishGame(report: PracticeReport? = null) {
        if (hasFinished) return
        hasFinished = true
        timerJob?.cancel()

        val state = _uiState.value
        if (!state.isPracticeMode && report == null) {
            scoreRepository.saveScore(
                username = authRepositoryUsername(),
                score = state.score,
                maxCombo = state.maxCombo,
                difficulty = state.difficulty
            )
        }

        _uiState.update {
            it.copy(
                isGameOver = true,
                practiceReport = report,
                userInput = ""
            )
        }

        viewModelScope.launch {
            _events.send(GameEvent.ShowResults(score = state.score, report = report))
        }
    }

    private fun advancePracticeOrFinish() {
        val state = _uiState.value
        val answeredCount = state.practiceQuestionsAnswered + 1
        if (answeredCount >= 10) {
            val report = practiceSessionManager.generateReport()
            finishGame(report)
        } else {
            val diffEnum = when (state.difficulty) {
                1 -> MathDifficulty.EASY
                2 -> MathDifficulty.INTERMEDIATE
                else -> MathDifficulty.ADVANCED
            }
            questionStartTime = System.currentTimeMillis()
            _uiState.update {
                it.copy(
                    practiceQuestionsAnswered = answeredCount,
                    userInput = "",
                    timeLeft = diffEnum.defaultTimeSeconds,
                    maxTimePerQuestion = diffEnum.defaultTimeSeconds,
                    currentQuestion = QuestionGenerator.generateQuestion(null, diffEnum)
                )
            }
            startTimer()
        }
    }

    fun finishPracticeEarly() {
        if (hasFinished) return
        val report = practiceSessionManager.generateReport()
        finishGame(report)
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

        questionStartTime = System.currentTimeMillis()
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
        val state = _uiState.value
        val remainingLives = state.lives - 1
        val diffEnum = when (state.difficulty) {
            1 -> MathDifficulty.EASY
            2 -> MathDifficulty.INTERMEDIATE
            else -> MathDifficulty.ADVANCED
        }

        if (remainingLives <= 0) {
            audioManager.playGameOverSound()
            finishGame(null)
        } else {
            audioManager.playIncorrectSound()
            questionStartTime = System.currentTimeMillis()
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

    private fun authRepositoryUsername(): String {
        return try {
            val authRepo = AuthRepository()
            authRepo.currentUserName
        } catch (_: Exception) {
            "Anthony966"
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        audioManager.release()
    }
}
