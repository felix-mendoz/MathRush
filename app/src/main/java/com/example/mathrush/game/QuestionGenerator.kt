package com.example.mathrush.game

import kotlin.random.Random

enum class MathOperationType {
    ADDITION, SUBTRACTION, MULTIPLICATION, MIXED
}

data class MathQuestion(
    val num1: Int,
    val num2: Int,
    val operation: Char, // '+', '-', '×'
    val correctAnswer: Int
)

object QuestionGenerator {

    fun generateQuestion(difficultyLevel: Int): MathQuestion {
        // A mayor nivel, rangos de números más amplios o mezcla de operaciones
        val maxNumber = 10 + (difficultyLevel * 8)

        val opChar = when (difficultyLevel) {
            1 -> listOf('+', '-').random()
            2 -> listOf('+', '-', '×').random()
            else -> listOf('+', '-', '×').random()
        }

        var num1 = Random.nextInt(1, maxNumber)
        var num2 = Random.nextInt(1, maxNumber)

        // Asegurar que en restas el resultado no sea negativo (opcional, pero ideal para agilidad rápida)
        if (opChar == '-' && num1 < num2) {
            val temp = num1
            num1 = num2
            num2 = temp
        }

        // Si es multiplicación, mantenemos los números más pequeños para agilizar el cálculo mental
        if (opChar == '×') {
            num1 = Random.nextInt(1, 10 + difficultyLevel)
            num2 = Random.nextInt(1, 10)
        }

        val answer = when (opChar) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '×' -> num1 * num2
            else -> num1 + num2
        }

        return MathQuestion(
            num1 = num1,
            num2 = num2,
            operation = opChar,
            correctAnswer = answer
        )
    }
}