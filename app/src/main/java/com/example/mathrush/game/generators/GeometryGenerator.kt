package com.example.mathrush.game.generators

import com.example.mathrush.game.*
import kotlin.random.Random

class GeometryGenerator : QuestionGeneratorStrategy {
    override fun generate(difficulty: MathDifficulty): MathQuestion {
        return when (difficulty) {
            MathDifficulty.EASY -> generateEasy()
            MathDifficulty.INTERMEDIATE -> generateIntermediate()
            MathDifficulty.ADVANCED -> generateAdvanced()
        }
    }

    private fun generateEasy(): MathQuestion {
        val base = Random.nextInt(3, 15)
        val height = Random.nextInt(3, 15)
        val area = base * height

        return MathQuestion(
            text = "Halla el área del rectángulo de base $base y altura $height:",
            latexExpression = "A = \\text{base} \\cdot \\text{altura}",
            correctAnswer = MathAnswer.Integer(area),
            category = MathCategory.GEOMETRY_TRIGONOMETRY,
            subcategory = "Áreas y perímetros",
            difficulty = MathDifficulty.EASY,
            explanation = listOf(
                "1. Fórmula del área del rectángulo: A = base X altura.",
                "2. A = $base X $height = $area.",
                "3. Resultado: $area"
            )
        )
    }

    private fun generateIntermediate(): MathQuestion {
        val triples = listOf(
            Triple(3, 4, 5),
            Triple(5, 12, 13),
            Triple(6, 8, 10),
            Triple(9, 12, 15)
        )
        val t = triples.random()

        return MathQuestion(
            text = "Halla la hipotenusa c usando el Teorema de Pitágoras:",
            latexExpression = "a = ${t.first}, \\, b = ${t.second}",
            correctAnswer = MathAnswer.Integer(t.third),
            category = MathCategory.GEOMETRY_TRIGONOMETRY,
            subcategory = "Teorema de Pitágoras",
            difficulty = MathDifficulty.INTERMEDIATE,
            explanation = listOf(
                "1. Teorema de Pitágoras: c^2 = a^2 + b^2.",
                "2. c^2 = ${t.first}^2 + ${t.second}^2 = ${t.first * t.first} + ${t.second * t.second} = ${t.third * t.third}.",
                "3. c = \\sqrt{${t.third * t.third}} = ${t.third}.",
                "4. Resultado: ${t.third}"
            )
        )
    }

    private fun generateAdvanced(): MathQuestion {
        // Trigonometric identity / evaluation e.g. sin^2(x) + cos^2(x) = 1 or sin(30) etc.
        // Let's ask for sin(30°) + cos(60°) = 0.5 + 0.5 = 1 or similar, or tan(45°) = 1.
        return MathQuestion(
            text = "Calcula el valor exacto de la expresión trigonométrica:",
            latexExpression = "\\tan(45^{\\circ}) + \\sin(30^{\\circ}) + \\cos(60^{\\circ})",
            correctAnswer = MathAnswer.Decimal(2.0, 0.01),
            category = MathCategory.GEOMETRY_TRIGONOMETRY,
            subcategory = "Identidades y valores trigonométricos",
            difficulty = MathDifficulty.ADVANCED,
            explanation = listOf(
                "1. \\tan(45^{\\circ}) = 1.",
                "2. \\sin(30^{\\circ}) = 0.5.",
                "3. \\cos(60^{\\circ}) = 0.5.",
                "4. Suma total: 1 + 0.5 + 0.5 = 2.0.",
                "5. Resultado: 2"
            )
        )
    }
}
