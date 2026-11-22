package com.example.mathrush.game

import org.junit.Assert.*
import org.junit.Test

class QuestionGeneratorsTest {

    @Test
    fun testArithmeticGeneration() {
        for (diff in MathDifficulty.entries) {
            val question = QuestionGenerator.generateQuestion(MathCategory.ARITHMETIC, diff)
            assertEquals(MathCategory.ARITHMETIC, question.category)
            assertEquals(diff, question.difficulty)
            assertNotNull(question.latexExpression)
            assertNotNull(question.correctAnswer)
            assertTrue(question.explanation.isNotEmpty())
        }
    }

    @Test
    fun testAlgebraGeneration() {
        for (diff in MathDifficulty.entries) {
            val question = QuestionGenerator.generateQuestion(MathCategory.ALGEBRA, diff)
            assertEquals(MathCategory.ALGEBRA, question.category)
            assertEquals(diff, question.difficulty)
            assertNotNull(question.latexExpression)
            assertNotNull(question.correctAnswer)
        }
    }

    @Test
    fun testDifferentialCalculusGeneration() {
        for (diff in MathDifficulty.entries) {
            val question = QuestionGenerator.generateQuestion(MathCategory.DIFFERENTIAL_CALCULUS, diff)
            assertEquals(MathCategory.DIFFERENTIAL_CALCULUS, question.category)
            assertEquals(diff, question.difficulty)
            assertNotNull(question.latexExpression)
        }
    }

    @Test
    fun testIntegralCalculusGeneration() {
        for (diff in MathDifficulty.entries) {
            val question = QuestionGenerator.generateQuestion(MathCategory.INTEGRAL_CALCULUS, diff)
            assertEquals(MathCategory.INTEGRAL_CALCULUS, question.category)
            assertEquals(diff, question.difficulty)
            assertNotNull(question.latexExpression)
        }
    }

    @Test
    fun testGeometryTrigonometryGeneration() {
        for (diff in MathDifficulty.entries) {
            val question = QuestionGenerator.generateQuestion(MathCategory.GEOMETRY_TRIGONOMETRY, diff)
            assertEquals(MathCategory.GEOMETRY_TRIGONOMETRY, question.category)
            assertEquals(diff, question.difficulty)
            assertNotNull(question.latexExpression)
        }
    }

    @Test
    fun testMathAnswerValidation() {
        val intAns = MathAnswer.Integer(42)
        assertTrue(intAns.validate("42"))
        assertFalse(intAns.validate("41"))

        val decAns = MathAnswer.Decimal(3.14, 0.01)
        assertTrue(decAns.validate("3.14"))
        assertTrue(decAns.validate("3.141"))
        assertFalse(decAns.validate("3.20"))

        val fracAns = MathAnswer.Fraction(3, 4)
        assertTrue(fracAns.validate("3/4"))
        assertTrue(fracAns.validate("0.75"))
        assertFalse(fracAns.validate("1/2"))
    }
}
