package com.example.mathrush.game.generators

import com.example.mathrush.game.MathDifficulty
import com.example.mathrush.game.MathQuestion

interface QuestionGeneratorStrategy {
    fun generate(difficulty: MathDifficulty): MathQuestion
}
