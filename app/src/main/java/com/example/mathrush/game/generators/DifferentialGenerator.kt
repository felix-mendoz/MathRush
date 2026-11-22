package com.example.mathrush.game.generators

import com.example.mathrush.game.*
import kotlin.random.Random

class DifferentialGenerator : QuestionGeneratorStrategy {
    override fun generate(difficulty: MathDifficulty): MathQuestion {
        return when (difficulty) {
            MathDifficulty.EASY -> generateEasy()
            MathDifficulty.INTERMEDIATE -> generateIntermediate()
            MathDifficulty.ADVANCED -> generateAdvanced()
        }
    }

    private fun generateEasy(): MathQuestion {
        val a = Random.nextInt(2, 6)
        val n = Random.nextInt(2, 4)
        val ans = a * n // derivative coefficient evaluated at x=1 or value of coefficient

        return MathQuestion(
            text = "Halla el coeficiente de la derivada en x = 1:",
            latexExpression = "\\frac{d}{dx} (${a}x^{$n})",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.DIFFERENTIAL_CALCULUS,
            subcategory = "Regla de la potencia",
            difficulty = MathDifficulty.EASY,
            explanation = listOf(
                "1. Aplica la regla de la potencia: d/dx(ax^n) = a \\cdot n \\cdot x^{n-1}.",
                "2. Derivada: ${a * n}x^{${n - 1}}.",
                "3. Evaluando en x = 1 se obtiene ${a * n}.",
                "4. Resultado: $ans"
            )
        )
    }

    private fun generateIntermediate(): MathQuestion {
        // Derivative of polynomial sum: a x^2 + b x
        val a = Random.nextInt(1, 5)
        val b = Random.nextInt(1, 10)
        // Derivative evaluated at x = 2: 2ax + b evaluated at x=2 -> 4a + b
        val ans = 4 * a + b

        return MathQuestion(
            text = "Calcula el valor de la derivada f'(x) en x = 2:",
            latexExpression = "f(x) = ${a}x^2 + ${b}x",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.DIFFERENTIAL_CALCULUS,
            subcategory = "Derivada de polinomios",
            difficulty = MathDifficulty.INTERMEDIATE,
            explanation = listOf(
                "1. Deriva término a término: f'(x) = ${2 * a}x + $b.",
                "2. Sustituye x = 2: f'(2) = ${2 * a}(2) + $b = ${4 * a} + $b.",
                "3. Resultado: $ans"
            )
        )
    }

    private fun generateAdvanced(): MathQuestion {
        // Derivative of trigonometric function e.g. a * sin(x) at x = 0 -> a * cos(0) = a
        val a = Random.nextInt(2, 10)
        val ans = a

        return MathQuestion(
            text = "Calcula f'(0) para la función:",
            latexExpression = "f(x) = ${a}\\sin(x)",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.DIFFERENTIAL_CALCULUS,
            subcategory = "Derivadas trigonométricas",
            difficulty = MathDifficulty.ADVANCED,
            explanation = listOf(
                "1. La derivada de \\sin(x) es \\cos(x).",
                "2. f'(x) = ${a}\\cos(x).",
                "3. Evalúa en x = 0: f'(0) = $a \\cdot \\cos(0) = $a \\cdot 1 = $a.",
                "4. Resultado: $ans"
            )
        )
    }
}
