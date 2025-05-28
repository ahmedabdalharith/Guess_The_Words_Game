package com.pyramid.questions.domain.model

import androidx.annotation.DrawableRes

data class GameLevel(
    val id: Int,                         // معرف المستوى
    val name: String,                    // اسم المستوى
    val wordCount: Int,                  // عدد الكلمات في المستوى
    val coinsToUnlock: Int,              // العملات المطلوبة لفتح المستوى
    val isUnlocked: Boolean,             // هل المستوى مفتوح؟
    val words: List<String>,             // قائمة الكلمات في المستوى
    val progress: String? = null,        // تقدم اللاعب في هذا المستوى (مثل "1/18")
    @DrawableRes val image: Int? = null, // صورة المستوى (اختيارية)
    val requiredStars: Int = 0,          // عدد النجوم المطلوبة لفتح المستوى
    val isRewardLevel: Boolean = false,  // هل هذا مستوى مكافأة؟
    val subtitle: String? = null,        // عنوان فرعي للمستوى (اختياري)
    val description: String? = null      // وصف إضافي للمستوى (اختياري)
)