package com.example.photos.ui.edit_photo

import android.graphics.ColorMatrix

enum class FilterMatrix(val title: String, val matrix: ColorMatrix) {
    ORIGINAL("Original", ColorMatrix().apply { setSaturation(1f) }),
    GRAYSCALE("Grayscale", ColorMatrix().apply { setSaturation(0f) }),
    SEPIA("Sepia", ColorMatrix().apply { setSaturation(0f); setScale(1f, 0.95f, 0.82f, 1f) }),
    INVERT("Invert",ColorMatrix().apply { set(floatArrayOf(
        -1.0f, 0.0f, 0.0f, 0.0f, 255f,
        0.0f, -1.0f, 0.0f, 0.0f, 255f,
        0.0f, 0.0f, -1.0f, 0.0f, 255f,
        0.0f, 0.0f, 0.0f, 1.0f, 0f
    )) }),
    BRIGHTNESS("Brightness", ColorMatrix().apply { set(floatArrayOf(
        1.2f, 0.0f, 0.0f, 0.0f, 0f,
        0.0f, 1.2f, 0.0f, 0.0f, 0f,
        0.0f, 0.0f, 1.2f, 0.0f, 0f,
        0.0f, 0.0f, 0.0f, 1.0f, 0f
    )) }),
    CONTRAST("Contrast", ColorMatrix().apply { set(floatArrayOf(
        2.0f, 0.0f, 0.0f, 0.0f, -255f,
        0.0f, 2.0f, 0.0f, 0.0f, -255f,
        0.0f, 0.0f, 2.0f, 0.0f, -255f,
        0.0f, 0.0f, 0.0f, 1.0f, 0f
    )) });
}