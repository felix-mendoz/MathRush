package com.example.mathrush.game.generators

import com.example.mathrush.game.*
import kotlin.random.Random

class AlgebraGenerator : QuestionGeneratorStrategy {
    override fun generate(difficulty: MathDifficulty): MathQuestion {
        return when (difficulty) {
            MathDifficulty.EASY -> generateEasy()
            MathDifficulty.INTERMEDIATE -> generateIntermediate()
            MathDifficulty.ADVANCED -> generateAdvanced()
        }
    }

    private fun generateEasy(): MathQuestion {
        val a = Random.nextInt(2, 6)
        val x = Random.nextInt(1, 10)
        val b = Random.nextInt(1, 15)
        val c = a * x + b

        return MathQuestion(
            text = "Halla el valor de x:",
            latexExpression = "${a}x + $b = $c",
            correctAnswer = MathAnswer.Integer(x),
            category = MathCategory.ALGEBRA,
            subcategory = "Ecuaciones 1er grado",
            difficulty = MathDifficulty.EASY,
            explanation = listOf(
                "1. Despeja el término con x restando $b en ambos lados: ${a}x = $c - $b = ${c - b}.",
                "2. Divide entre $a: x = ${c - b} / $a = $x.",
                "3. Resultado: x = $x"
            )
        )
    }

    private fun generateIntermediate(): MathQuestion {
        // a(x + b) = c(x + d) or similar
        // Let's do: 2(x + b) = x + c
        val b = Random.nextInt(1, 5)
        val x = Random.nextInt(2, 10)
        val c = 2 * (x + b) - x

        return MathQuestion(
            text = "Resuelve la ecuación:",
            latexExpression = "2(x + $b) - x = $c",
            correctAnswer = MathAnswer.Integer(x),
            category = MathCategory.ALGEBRA,
            subcategory = "Ecuaciones con paréntesis",
            difficulty = MathDifficulty.INTERMEDIATE,
            explanation = listOf(
                "1. Aplica la propiedad distributiva: 2x + ${2*b} - x = $c.",
                "2. Agrupa términos semejantes: x + ${2*b} = $c.",
                "3. Despeja x: x = $c - ${2*b} = $x.",
                "4. Resultado: x = $x"
            )
        )
    }

    private fun generateAdvanced(): MathQuestion {
        // Quadratic equation: (x - r1)(x - r2) = 0 -> x^2 - (r1+r2)x + r1*r2 = 0
        val r1 = Random.nextInt(1, 6)
        val r2 = Random.nextInt(1, 6)
        val sum = r1 + r2
        val prod = r1 * r2

        return MathQuestion(
            text = "Halla la raíz positiva mayor de la ecuación cuadrática:",
            latexExpression = "x^2 - ${sum}x + $prod = 0",
            correctAnswer = MathAnswer.Integer(maxOf(r1, r2)),
            category = MathCategory.ALGEBRA,
            subcategory = "Ecuaciones cuadráticas",
            difficulty = MathDifficulty.ADVANCED,
            explanation = listOf(
                "1. Factoriza la ecuación cuadrática como (x - $r1)(x - $r2) = 0.",
                "2. Las soluciones son x = $r1 y x = $r2.",
                "3. La raíz mayor es ${maxOf(r1, r2)}.",
                "4. Resultado: ${maxOf(r1, r2)}"
            )
        )
    }
}
