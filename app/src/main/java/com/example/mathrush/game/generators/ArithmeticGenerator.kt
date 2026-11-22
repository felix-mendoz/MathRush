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
            val t = n1; n1 = n2; n2 = t
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
                "1. Identifica la operación principal: ${if (op == '+') "suma" else "resta"}.",
                "2. Opera $n1 $op $n2.",
                "3. Resultado: $ans"
            )
        )
    }

    private fun generateIntermediate(): MathQuestion {
        val ops = listOf('+', '-', '×', '÷')
        val op = ops.random()
        var n1: Int
        var n2: Int
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
                n1 = Random.nextInt(10, 50)
                n2 = Random.nextInt(10, 50)
                if (op == '-' && n1 < n2) { val t = n1; n1 = n2; n2 = t }
                ans = if (op == '+') n1 + n2 else n1 - n2
            }
        }

        return MathQuestion(
            text = "Resuelve la operación:",
            latexExpression = "$n1 \\cdot $op $n2".replace("· ·", "·"),
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.ARITHMETIC,
            subcategory = "Operaciones intermedias",
            difficulty = MathDifficulty.INTERMEDIATE,
            explanation = listOf(
                "1. Analiza la expresión dada.",
                "2. Aplica la regla de la operación $op entre $n1 y $n2.",
                "3. Resultado final: $ans"
            )
        )
    }

    private fun generateAdvanced(): MathQuestion {
        // Combined operations: (a + b) * c - d
        val a = Random.nextInt(2, 10)
        val b = Random.nextInt(2, 10)
        val c = Random.nextInt(2, 6)
        val d = Random.nextInt(1, 20)
        val ans = (a + b) * c - d

        return MathQuestion(
            text = "Resuelve la operación combinada:",
            latexExpression = "( $a + $b ) \\cdot $c - $d",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.ARITHMETIC,
            subcategory = "Operaciones combinadas",
            difficulty = MathDifficulty.ADVANCED,
            explanation = listOf(
                "1. Primero resuelve la operación dentro del paréntesis: $a + $b = ${a + b}.",
                "2. Multiplica por $c: ${a + b} \\cdot $c = ${(a + b) * c}.",
                "3. Resta $d: ${(a + b) * c} - $d = $ans.",
                "4. Resultado: $ans"
            )
        )
    }
}
