package com.example.mathrush.game

import kotlin.random.Random

enum class MathCategory(val title: String) {
    ARITHMETIC("Aritmética"),
    ALGEBRA("Álgebra"),
    DIFFERENTIAL_CALCULUS("Cálculo Diferencial"),
    INTEGRAL_CALCULUS("Cálculo Integral"),
    GEOMETRY_TRIGONOMETRY("Geometría y Trigonometría")
}

enum class MathDifficulty(val title: String, val defaultTimeSeconds: Int) {
    EASY("Fácil", 15),
    INTERMEDIATE("Intermedio", 10),
    ADVANCED("Avanzado", 20)
}

data class MathQuestion(
    val questionText: String,
    val correctAnswer: Int,
    val category: MathCategory,
    val subcategory: String,
    val difficulty: MathDifficulty
)

object QuestionGenerator {

    fun generateQuestion(category: MathCategory? = null, difficulty: MathDifficulty = MathDifficulty.INTERMEDIATE): MathQuestion {
        val selectedCategory = category ?: MathCategory.entries.random()
        return when (selectedCategory) {
            MathCategory.ARITHMETIC -> generateArithmetic(difficulty)
            MathCategory.ALGEBRA -> generateAlgebra(difficulty)
            MathCategory.DIFFERENTIAL_CALCULUS -> generateDifferential(difficulty)
            MathCategory.INTEGRAL_CALCULUS -> generateIntegral(difficulty)
            MathCategory.GEOMETRY_TRIGONOMETRY -> generateGeometry(difficulty)
        }
    }

    private fun generateArithmetic(difficulty: MathDifficulty): MathQuestion {
        val subcategories = listOf("Operaciones básicas", "Porcentajes", "Potencias y Raíces")
        val sub = subcategories.random()
        return when (sub) {
            "Porcentajes" -> {
                val base = listOf(50, 100, 200, 300, 400, 500).random()
                val pct = listOf(10, 20, 25, 50).random()
                val ans = (base * pct) / 100
                MathQuestion(
                    questionText = "$pct% de $base = ?",
                    correctAnswer = ans,
                    category = MathCategory.ARITHMETIC,
                    subcategory = "Porcentajes",
                    difficulty = difficulty
                )
            }
            "Potencias y Raíces" -> {
                if (Random.nextBoolean()) {
                    val base = Random.nextInt(2, 8)
                    MathQuestion(
                        questionText = "$base³ = ?",
                        correctAnswer = base * base * base,
                        category = MathCategory.ARITHMETIC,
                        subcategory = "Potencias y Raíces",
                        difficulty = difficulty
                    )
                } else {
                    val root = Random.nextInt(2, 12)
                    val sq = root * root
                    MathQuestion(
                        questionText = "√$sq = ?",
                        correctAnswer = root,
                        category = MathCategory.ARITHMETIC,
                        subcategory = "Potencias y Raíces",
                        difficulty = difficulty
                    )
                }
            }
            else -> {
                val maxNum = when (difficulty) {
                    MathDifficulty.EASY -> 20
                    MathDifficulty.INTERMEDIATE -> 50
                    MathDifficulty.ADVANCED -> 100
                }
                val ops = when (difficulty) {
                    MathDifficulty.EASY -> listOf('+', '-')
                    MathDifficulty.INTERMEDIATE -> listOf('+', '-', '×')
                    MathDifficulty.ADVANCED -> listOf('+', '-', '×', '÷')
                }
                val op = ops.random()
                var n1 = Random.nextInt(1, maxNum)
                var n2 = Random.nextInt(1, maxNum)

                if (op == '-' && n1 < n2) {
                    val t = n1; n1 = n2; n2 = t
                }
                if (op == '×') {
                    n1 = Random.nextInt(1, 12)
                    n2 = Random.nextInt(1, 12)
                }
                if (op == '÷') {
                    n2 = Random.nextInt(1, 10)
                    val quotient = Random.nextInt(1, 12)
                    n1 = n2 * quotient
                }

                val ans = when (op) {
                    '+' -> n1 + n2
                    '-' -> n1 - n2
                    '×' -> n1 * n2
                    '÷' -> n1 / n2
                    else -> n1 + n2
                }

                MathQuestion(
                    questionText = "$n1 $op $n2 = ?",
                    correctAnswer = ans,
                    category = MathCategory.ARITHMETIC,
                    subcategory = "Operaciones básicas",
                    difficulty = difficulty
                )
            }
        }
    }

    private fun generateAlgebra(difficulty: MathDifficulty): MathQuestion {
        val a = Random.nextInt(2, 6)
        val x = Random.nextInt(1, 10)
        val b = Random.nextInt(1, 20)
        val c = a * x + b

        return MathQuestion(
            questionText = "${a}x + $b = $c\nHalla x:",
            correctAnswer = x,
            category = MathCategory.ALGEBRA,
            subcategory = "Ecuaciones 1er grado",
            difficulty = difficulty
        )
    }

    private fun generateDifferential(difficulty: MathDifficulty): MathQuestion {
        val a = Random.nextInt(1, 5)
        val n = Random.nextInt(2, 4)
        val ans = a * n

        return MathQuestion(
            questionText = "d/dx (${a}x^$n) en x = 1\n(Coeficiente derivado):",
            correctAnswer = ans,
            category = MathCategory.DIFFERENTIAL_CALCULUS,
            subcategory = "Derivadas básicas",
            difficulty = difficulty
        )
    }

    private fun generateIntegral(difficulty: MathDifficulty): MathQuestion {
        val a = Random.nextInt(1, 4) * 2
        val upper = Random.nextInt(1, 4)
        val ans = (a / 2) * (upper * upper)

        return MathQuestion(
            questionText = "∫₀^$upper (${a}x) dx\n(Valor exacto):",
            correctAnswer = ans,
            category = MathCategory.INTEGRAL_CALCULUS,
            subcategory = "Integrales definidas",
            difficulty = difficulty
        )
    }

    private fun generateGeometry(difficulty: MathDifficulty): MathQuestion {
        val triples = listOf(
            Triple(3, 4, 5),
            Triple(5, 12, 13),
            Triple(6, 8, 10),
            Triple(9, 12, 15)
        )
        val t = triples.random()
        return MathQuestion(
            questionText = "Catetos: a=${t.first}, b=${t.second}\nHipotenusa c:",
            correctAnswer = t.third,
            category = MathCategory.GEOMETRY_TRIGONOMETRY,
            subcategory = "Teorema de Pitágoras",
            difficulty = difficulty
        )
    }
}
