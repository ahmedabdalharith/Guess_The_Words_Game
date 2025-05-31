package com.pyramid.questions.domain.model

import org.json.JSONObject


data class LetterData(
    val id: Int,
    val letter: Char,
    val isSelected: Boolean = false,
    val isDeleted: Boolean = false
)
data class Question(
    val questionText: String = "",
    val questionId: String = "",
    val imageUrl: String = "",
    val answer: String = "",
    val lettersPool: List<Char> = emptyList(),
    val hint: List<Char> = emptyList(),
    val level: String = "",
    val category: QuestionCategory = QuestionCategory.GENERAL,
    val selectedLetters: List<LetterData> = emptyList(),
    val availableLetters: List<LetterData> = emptyList()
) {
    fun getQuestionJson(): String {
        return """
        {
            "questionText": "$questionText",
            "questionId": "$questionId",
            "imageUrl": "$imageUrl",
            "answer": "$answer",
            "lettersPool": "${lettersPool.joinToString("")}",
            "hint": "${hint.joinToString("")}",
            "level": "$level",
            "category": "${category.name}",
            "selectedLetters": ${selectedLetters.map { it.letter }},
            "availableLetters": ${availableLetters.map { it.letter }}
        }
    """.trimIndent()
    }
    fun fromJsonToQuestion(json: String): Question {
        val jsonObject = JSONObject(json)

        // Parse basic fields
        val questionText = jsonObject.getString("questionText")
        val questionId = jsonObject.getString("questionId")
        val imageUrl = jsonObject.getString("imageUrl")
        val answer = jsonObject.getString("answer")
        val level = jsonObject.getString("level")
        val categoryName = jsonObject.getString("category")

        // Parse lettersPool back to List<Char>
        val lettersPoolString = jsonObject.getString("lettersPool")
        val lettersPool = lettersPoolString.toList()

        // Parse hint back to List<Char>
        val hintString = jsonObject.getString("hint")
        val hint = hintString.toList()

        // Parse selectedLetters array
        val selectedLettersArray = jsonObject.getJSONArray("selectedLetters")
        val selectedLetters = mutableListOf<Char>()
        for (i in 0 until selectedLettersArray.length()) {
            val letterChar = selectedLettersArray.getString(i).first()
            selectedLetters.add(letterChar) // Assuming Letter has a constructor that takes Char
        }

        // Parse availableLetters array
        val availableLettersArray = jsonObject.getJSONArray("availableLetters")
        val availableLetters = mutableListOf<Char>()
        for (i in 0 until availableLettersArray.length()) {
            val letterChar = availableLettersArray.getString(i).first()
            availableLetters.add(letterChar)
        }

        // Parse category enum (assuming Category is an enum)
        val category = QuestionCategory.valueOf(categoryName)

        // Create and return Question object
        return Question(
            questionText = questionText,
            questionId = questionId,
            imageUrl = imageUrl,
            answer = answer,
            lettersPool = lettersPool,
            hint = hint,
            level = level,
            category = category,
        )
    }

    fun isCorrectAnswer(userAnswer: String): Boolean {
        return userAnswer.equals(answer, ignoreCase = true)
    }

    fun isCurrentAnswerCorrect(): Boolean {
        val currentAnswer = selectedLetters.joinToString("") { it.letter.toString() }
        return isCorrectAnswer(currentAnswer)
    }

    fun isComplete(): Boolean {
        return questionId.isNotBlank() &&
                imageUrl.isNotBlank() &&
                answer.isNotBlank() &&
                lettersPool.isNotEmpty() &&
                hint.isNotEmpty() &&
                level.isNotBlank()
    }

    fun isError(): Boolean {
        return questionId.isBlank() ||
                imageUrl.isBlank() ||
                answer.isBlank() ||
                lettersPool.isEmpty() ||
                hint.isEmpty() ||
                level.isBlank()
    }

    fun initializeAvailableLetters(): Question {
        val letters = lettersPool.mapIndexed { index, char ->
            LetterData(
                id = index,
                letter = char,
                isSelected = false,
                isDeleted = false
            )
        }
        return this.copy(availableLetters = letters, selectedLetters = emptyList())
    }

    fun selectLetter(letterData: LetterData): Question {
        if (letterData.isSelected || letterData.isDeleted || selectedLetters.size >= answer.length) {
            return this
        }

        val updatedAvailableLetters = availableLetters.map { letter ->
            if (letter.id == letterData.id) {
                letter.copy(isSelected = true)
            } else letter
        }

        val updatedSelectedLetters = selectedLetters + letterData.copy(isSelected = true)

        return this.copy(
            availableLetters = updatedAvailableLetters,
            selectedLetters = updatedSelectedLetters
        )
    }

    fun deleteLetter(): Question {
        if (selectedLetters.isEmpty()) return this

        val lastLetter = selectedLetters.last()
        val updatedAvailableLetters = availableLetters.map { letter ->
            if (letter.id == lastLetter.id) {
                letter.copy(isSelected = false)
            } else letter
        }

        val updatedSelectedLetters = selectedLetters.dropLast(1)

        return this.copy(
            availableLetters = updatedAvailableLetters,
            selectedLetters = updatedSelectedLetters
        )
    }

    fun clearAllLetters(): Question {
        val updatedAvailableLetters = availableLetters.map { letter ->
            letter.copy(isSelected = false)
        }

        return this.copy(
            availableLetters = updatedAvailableLetters,
            selectedLetters = emptyList()
        )
    }

    fun deleteRandomLetters(count: Int): Question {
        val incorrectLetters = availableLetters.filter { letter ->
            !letter.isSelected && !letter.isDeleted && letter.letter !in answer.toCharArray()
        }

        if (incorrectLetters.isEmpty()) return this

        val lettersToDelete = incorrectLetters.shuffled().take(count)
        val updatedAvailableLetters = availableLetters.map { letter ->
            if (letter.id in lettersToDelete.map { it.id }) {
                letter.copy(isDeleted = true)
            } else letter
        }

        return this.copy(availableLetters = updatedAvailableLetters)
    }

    fun useHint(): Question {
        val correctLetters = answer.toCharArray().distinct()
        val availableCorrectLetters = availableLetters.filter {
            it.letter in correctLetters && !it.isSelected && !it.isDeleted
        }

        if (availableCorrectLetters.isEmpty() || selectedLetters.size >= answer.length) {
            return this
        }

        val randomCorrectLetter = availableCorrectLetters.random()
        return selectLetter(randomCorrectLetter)
    }

    fun getCurrentAnswer(): String {
        return selectedLetters.joinToString("") { it.letter.toString() }
    }

    fun isGameWon(): Boolean {
        return selectedLetters.size == answer.length && isCurrentAnswerCorrect()
    }

    fun hasSelectedLetters(): Boolean {
        return selectedLetters.isNotEmpty()
    }
}


enum class QuestionCategory {
    CELEBRITIES,
    ANIMALS,
    OBJECTS,
    FOOD,
    MOVIES,
    SPORTS,
    NATURE,
    TECHNOLOGY,
    BRANDS,
    CHARACTERS,
    GENERAL,
}