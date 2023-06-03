package com.example.languagelearningapp.ui.common

import android.view.HapticFeedbackConstants
import android.view.View

fun View.reallyPerformHapticFeedback(feedbackConstant: Int) {
    isHapticFeedbackEnabled = true
    performHapticFeedback(feedbackConstant, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
}