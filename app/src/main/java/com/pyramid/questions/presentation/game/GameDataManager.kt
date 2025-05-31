package com.pyramid.questions.presentation.game

import com.pyramid.questions.R
import com.pyramid.questions.domain.model.LetterData


class GameDataManager {

    private val words = listOf(
        "مصر", "السعودية", "الإمارات", "قطر", "الكويت",
        "العراق", "الأردن", "لبنان", "فلسطين", "سوريا",
        "اليمن", "عمان", "البحرين", "المغرب", "الجزائر"
    )
    private val hintImages = listOf(
        R.drawable.coin_ic,
        R.drawable.refresh_ic,
        R.drawable.idea_ic,
        R.drawable.close_ic,
        R.drawable.arrow_forward_ic
    )
    private val hints = listOf(
        "أهرامات الجيزة",
        "مكة المكرمة والمدينة المنورة",
        "برج خليفة",
        "استضافة كأس العالم 2022",
        "أبراج الكويت",
        "بلاد الرافدين",
        "البتراء",
        "أرز لبنان",
        "القدس",
        "دمشق أقدم عاصمة مأهولة",
        "باب المندب",
        "سلطنة عُمان",
        "جزيرة في الخليج العربي",
        "جبال الأطلس",
        "صحراء الجزائر"
    )

    private var currentWordIndex = 0
    private var _score = 0
    private var _level = 1
    private var maxLevel = 15
    private var targetWord = words[currentWordIndex]
    private var showSuccess = false
    private var showError = false
    private var showHint = false
    private var _selectedLetters = mutableListOf<LetterData>()
    private var _availableLetters = mutableListOf<LetterData>()

    init {
        resetGame()
    }

    fun resetGame() {
        currentWordIndex = 0
        _score = 0
        _level = 1
        targetWord = words[currentWordIndex]
        _selectedLetters.clear()
        generateAvailableLetters()
        showSuccess = false
        showError = false
        showHint = false
    }

    fun nextWord() {
        currentWordIndex = (currentWordIndex + 1) % words.size
        _level++
        targetWord = words[currentWordIndex]
        _selectedLetters.clear()
        generateAvailableLetters()
    }

    private fun generateAvailableLetters() {
        _availableLetters.clear()

        val wordChars = targetWord.toList()

        val additionalChars = listOf('أ', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض', 'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل', 'م', 'ن', 'ه', 'و', 'ي')
            .filter { !wordChars.contains(it) }
            .shuffled()
            .take(10 - wordChars.size)

        // دمج الأحرف وإنشاء قائمة _availableLetters
        val allChars = (wordChars + additionalChars).shuffled()
        allChars.forEachIndexed { index, char ->
            _availableLetters.add(LetterData(id = index, letter = char))
        }
    }

    // اختيار حرف
    fun selectLetter(letterData: LetterData) {
        if (letterData.isSelected || letterData.isDeleted || _selectedLetters.size >= targetWord.length) {
            return
        }

        // تحديث حالة الحرف إلى مختار
        val index = _availableLetters.indexOfFirst { it.id == letterData.id }
        if (index != -1) {
            _availableLetters[index] = _availableLetters[index].copy(isSelected = true)
            _selectedLetters.add(_availableLetters[index])
        }

        // التحقق إذا تم تكوين الكلمة الصحيحة
        checkAnswer()
    }

    // التحقق من الإجابة
    private fun checkAnswer() {
        if (_selectedLetters.size == targetWord.length) {
            val word = _selectedLetters.joinToString("") { it.letter.toString() }
            if (word == targetWord) {
                // الإجابة صحيحة
                _score += 50
                showSuccess = true

                // انتظر قليلاً ثم انتقل إلى الكلمة التالية
                // هذا يتم التعامل معه في مكون العرض مع LaunchedEffect
            } else {
                // الإجابة خاطئة
                showError = true
                // إعادة تعيين الأحرف المختارة
                clearAllLetters()
            }
        }
    }

    // حذف آخر حرف تم اختياره
    fun deleteLetter() {
        if (_selectedLetters.isNotEmpty()) {
            val lastLetter = _selectedLetters.last()
            _selectedLetters.removeAt(_selectedLetters.lastIndex)
            val index = _availableLetters.indexOfFirst { it.id == lastLetter.id }
            if (index != -1) {
                _availableLetters[index] = _availableLetters[index].copy(isSelected = false)
            }
        }
    }

    fun clearAllLetters() {
        _selectedLetters.forEach { selected ->
            val index = _availableLetters.indexOfFirst { it.id == selected.id }
            if (index != -1) {
                _availableLetters[index] = _availableLetters[index].copy(isSelected = false)
            }
        }
        _selectedLetters.clear()
    }
    fun deleteRandomLetters(count: Int) {
        // تحديد الحروف المتاحة التي لم يتم اختيارها أو حذفها بعد
        val availableUnselectedLetters = _availableLetters.filter {
            !it.isSelected && !it.isDeleted
        }

        // تحديد الحروف غير الصحيحة (التي لا تنتمي للكلمة المستهدفة)
        val incorrectLetters = availableUnselectedLetters.filter { letter ->
            !targetWord.contains(letter.letter)
        }

        // تحديد عدد الحروف التي سيتم حذفها (لا يتجاوز عدد الحروف غير الصحيحة المتاحة)
        val deleteCount = minOf(count, incorrectLetters.size)

        // اختيار عدد محدد من الحروف غير الصحيحة بشكل عشوائي
        val lettersToDelete = incorrectLetters.shuffled().take(deleteCount)

        // تعليم الحروف المختارة كمحذوفة
        lettersToDelete.forEach { letterToDelete ->
            val index = _availableLetters.indexOfFirst { it.id == letterToDelete.id }
            if (index != -1) {
                // تحديث حالة الحرف إلى محذوف
                _availableLetters[index] = _availableLetters[index].copy(isDeleted = true)
            }
        }
    }

    // تخطي الكلمة الحالية
    fun skipWord() {
        // خصم نقاط مقابل تخطي الكلمة
        _score = maxOf(0, _score - 10)
        nextWord()
    }

    // إظهار تلميح
    fun showHint() {
        showHint = true
        _score = maxOf(0, _score - 5)
    }

    fun getGameData(): GameData {
        return GameData(
            targetWord = targetWord,
            score = _score,
            level = _level,
            maxLevel = maxLevel,
            selectedLetters = _selectedLetters,
            availableLetters = _availableLetters,
            hintImageResId = hintImages[currentWordIndex % hintImages.size],
            hintText = hints[currentWordIndex]
        )
    }

    // دوال للحصول على وإعداد حالات العرض
    fun getShowSuccess() = showSuccess
    fun setShowSuccess(value: Boolean) { showSuccess = value }

    fun getShowError() = showError
    fun setShowError(value: Boolean) { showError = value }

    fun getShowHint() = showHint
    fun setShowHint(value: Boolean) { showHint = value }

}

/**
 * فئة لتجميع بيانات اللعبة
 */
data class GameData(
    val targetWord: String,
    val score: Int,
    val level: Int,
    val maxLevel: Int,
    val selectedLetters: List<LetterData>,
    val availableLetters: List<LetterData>,
    val hintImageResId: Int,
    val hintText: String
)