
package com.example.mathrush.game.generators

import com.example.mathrush.game.*
import kotlin.random.Random

class ArithmeticGenerator : QuestionGeneratorStrategy {

    override fun generate(difficulty: MathDifficulty): MathQuestion {
        return when (difficulty) {
            MathDifficulty.EASY -> generateEasy()
            MathDifficulty.INTERMEDIATE -> generateIntermediate()
            MathDifficulty.ADVANCED -> generateAdvanced()
        }
    }

    private fun generateEasy(): MathQuestion {
        val op = listOf('+', '-').random()

        var n1 = Random.nextInt(1, 20)
        var n2 = Random.nextInt(1, 20)

        if (op == '-' && n1 < n2) {
            val temp = n1
            n1 = n2
            n2 = temp
        }

        val ans = if (op == '+') n1 + n2 else n1 - n2

        return MathQuestion(
            text = "Calcula el resultado:",
            latexExpression = "$n1 $op $n2",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.ARITHMETIC,
            subcategory = "Suma y resta básica",
            difficulty = MathDifficulty.EASY,
            explanation = listOf(
                "1. Identifica la operación: ${if (op == '+') "suma" else "resta"}.",
                "2. Calcula: $n1 $op $n2.",
                "3. Resultado: $ans."
            )
        )
    }

    private fun generateIntermediate(): MathQuestion {
        val op = listOf('+', '-', '×', '÷').random()

        val n1: Int
        val n2: Int
        val ans: Int

        when (op) {
            '×' -> {
                n1 = Random.nextInt(2, 12)
                n2 = Random.nextInt(2, 12)
                ans = n1 * n2
            }

            '÷' -> {
                n2 = Random.nextInt(2, 10)
                val quotient = Random.nextInt(2, 12)
                n1 = n2 * quotient
                ans = quotient
            }

            else -> {
                var a = Random.nextInt(10, 50)
                var b = Random.nextInt(10, 50)

                if (op == '-' && a < b) {
                    val temp = a
                    a = b
                    b = temp
                }

                n1 = a
                n2 = b
                ans = if (op == '+') n1 + n2 else n1 - n2
            }
        }

        return MathQuestion(
            text = "Resuelve la siguiente operación:",
            latexExpression = "$n1 $op $n2",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.ARITHMETIC,
            subcategory = "Operaciones intermedias",
            difficulty = MathDifficulty.INTERMEDIATE,
            explanation = listOf(
                "1. Identifica la operación entre $n1 y $n2.",
                "2. Aplica la operación $op.",
                "3. Calcula: $n1 $op $n2 = $ans."
            )
        )
    }

    private fun generateAdvanced(): MathQuestion {
        val a = Random.nextInt(2, 10)
        val b = Random.nextInt(2, 10)
        val c = Random.nextInt(2, 6)
        val d = Random.nextInt(1, 20)

        val parenthesisResult = a + b
        val multiplicationResult = parenthesisResult * c
        val ans = multiplicationResult - d

        return MathQuestion(
            text = "Resuelve la siguiente operación combinada:",
            latexExpression = "($a + $b) × $c - $d",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.ARITHMETIC,
            subcategory = "Operaciones combinadas",
            difficulty = MathDifficulty.ADVANCED,
            explanation = listOf(
                "1. Resuelve primero el paréntesis: $a + $b = $parenthesisResult.",
                "2. Multiplica el resultado por $c: $parenthesisResult × $c = $multiplicationResult.",
                "3. Resta $d: $multiplicationResult - $d = $ans.",
                "4. Resultado final: $ans."
            )
        )
    }
}