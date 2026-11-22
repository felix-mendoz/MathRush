package com.example.mathrush.game.generators

import com.example.mathrush.game.*
import kotlin.random.Random

class IntegralGenerator : QuestionGeneratorStrategy {
    override fun generate(difficulty: MathDifficulty): MathQuestion {
        return when (difficulty) {
            MathDifficulty.EASY -> generateEasy()
            MathDifficulty.INTERMEDIATE -> generateIntermediate()
            MathDifficulty.ADVANCED -> generateAdvanced()
        }
    }

    private fun generateEasy(): MathQuestion {
        val a = Random.nextInt(2, 10)
        val ans = a

        return MathQuestion(
            text = "Calcula la integral definida:",
            latexExpression = "\\int_{0}^{1} ($a) \\, dx",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.INTEGRAL_CALCULUS,
            subcategory = "Integrales inmediatas",
            difficulty = MathDifficulty.EASY,
            explanation = listOf(
                "1. La integral de una constante a es ax.",
                "2. Evalúa de 0 a 1: a(1) - a(0) = $a.",
                "3. Resultado: $ans"
            )
        )
    }

    private fun generateIntermediate(): MathQuestion {
        val a = Random.nextInt(1, 4) * 2
        val upper = Random.nextInt(2, 5)
        val ans = (a / 2) * (upper * upper)

        return MathQuestion(
            text = "Calcula la integral definida:",
            latexExpression = "\\int_{0}^{$upper} (${a}x) \\, dx",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.INTEGRAL_CALCULUS,
            subcategory = "Integrales definidas",
            difficulty = MathDifficulty.INTERMEDIATE,
            explanation = listOf(
                "1. La integral de ax es (a/2)x^2.",
                "2. Sustituye los límites 0 y $upper: (${a}/2)($upper)^2 - 0.",
                "3. ${a / 2} \\cdot ${upper * upper} = $ans.",
                "4. Resultado: $ans"
            )
        )
    }

    private fun generateAdvanced(): MathQuestion {
        val upper = 3
        val a = 3
        val ans = (a / 3) * (upper * upper * upper)

        return MathQuestion(
            text = "Calcula la integral definida:",
            latexExpression = "\\int_{0}^{$upper} (${a}x^2) \\, dx",
            correctAnswer = MathAnswer.Integer(ans),
            category = MathCategory.INTEGRAL_CALCULUS,
            subcategory = "Integrales polinómicas avanzadas",
            difficulty = MathDifficulty.ADVANCED,
            explanation = listOf(
                "1. La regla de la potencia para integrales: \\int ax^2 dx = \\frac{a}{3}x^3.",
                "2. Aquí tenemos \\frac{$a}{3}x^3 = x^3.",
                "3. Evaluamos de 0 a $upper: $upper^3 - 0^3 = $ans.",
                "4. Resultado: $ans"
            )
        )
    }
}
