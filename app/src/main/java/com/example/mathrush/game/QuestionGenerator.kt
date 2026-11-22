package com.example.mathrush.game

import com.example.mathrush.game.generators.*

enum class MathCategory(val title: String) {
    ARITHMETIC("Aritmética"),
    ALGEBRA("Álgebra"),
    DIFFERENTIAL_CALCULUS("Cálculo Diferencial"),
    INTEGRAL_CALCULUS("Cálculo Integral"),
    GEOMETRY_TRIGONOMETRY("Geometría y Trigonometría")
}

enum class MathDifficulty(val title: String, val defaultTimeSeconds: Int) {
    EASY("Fácil", 20),
    INTERMEDIATE("Intermedio", 15),
    ADVANCED("Avanzado", 25)
}

sealed class MathAnswer {
    data class Integer(val value: Int) : MathAnswer()
    data class Decimal(val value: Double, val tolerance: Double = 0.01) : MathAnswer()
    data class Fraction(val numerator: Int, val denominator: Int) : MathAnswer()
    data class Text(val value: String) : MathAnswer()
    data class Algebraic(val expression: String) : MathAnswer()

    fun validate(input: String): Boolean {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return false
        return when (this) {
            is Integer -> trimmed.toIntOrNull() == value
            is Decimal -> {
                val d = trimmed.toDoubleOrNull()
                if (d != null) kotlin.math.abs(d - value) <= tolerance else false
            }
            is Fraction -> {
                if (trimmed.contains("/")) {
                    val parts = trimmed.split("/")
                    if (parts.size == 2) {
                        val num = parts[0].trim().toIntOrNull()
                        val den = parts[1].trim().toIntOrNull()
                        if (num != null && den != null && den != 0) {
                            return num * denominator == numerator * den
                        }
                    }
                }
                val d = trimmed.toDoubleOrNull()
                if (d != null) {
                    val expectedVal = if (denominator == 0) 0.0 else numerator.toDouble() / denominator.toDouble()
                    kotlin.math.abs(d - expectedVal) <= 0.01
                } else {
                    false
                }
            }
            is Text -> trimmed.equals(value, ignoreCase = true)
            is Algebraic -> trimmed.replace("\\s".toRegex(), "").equals(expression.replace("\\s".toRegex(), ""), ignoreCase = true)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Integer -> value.toString()
            is Decimal -> value.toString()
            is Fraction -> "$numerator/$denominator"
            is Text -> value
            is Algebraic -> expression
        }
    }
}

data class MathQuestion(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val latexExpression: String?,
    val correctAnswer: MathAnswer,
    val category: MathCategory,
    val subcategory: String,
    val difficulty: MathDifficulty,
    val explanation: List<String> = emptyList()
)

object QuestionGenerator {

    private val arithmeticGenerator = ArithmeticGenerator()
    private val algebraGenerator = AlgebraGenerator()
    private val differentialGenerator = DifferentialGenerator()
    private val integralGenerator = IntegralGenerator()
    private val geometryGenerator = GeometryGenerator()

    fun generateQuestion(category: MathCategory? = null, difficulty: MathDifficulty = MathDifficulty.INTERMEDIATE): MathQuestion {
        val selectedCategory = category ?: when (difficulty) {
            MathDifficulty.EASY -> MathCategory.ARITHMETIC
            MathDifficulty.INTERMEDIATE -> listOf(MathCategory.ARITHMETIC, MathCategory.ALGEBRA, MathCategory.GEOMETRY_TRIGONOMETRY).random()
            MathDifficulty.ADVANCED -> listOf(MathCategory.ARITHMETIC, MathCategory.ALGEBRA, MathCategory.INTEGRAL_CALCULUS, MathCategory.GEOMETRY_TRIGONOMETRY).random()
        }

        val generator: QuestionGeneratorStrategy = when (selectedCategory) {
            MathCategory.ARITHMETIC -> arithmeticGenerator
            MathCategory.ALGEBRA -> algebraGenerator
            MathCategory.DIFFERENTIAL_CALCULUS -> differentialGenerator
            MathCategory.INTEGRAL_CALCULUS -> integralGenerator
            MathCategory.GEOMETRY_TRIGONOMETRY -> geometryGenerator
        }

        return generator.generate(difficulty)
    }
}
