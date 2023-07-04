package com.example.photos.ui.edit_photo

import android.graphics.ColorMatrix

enum class FilterMatrix(val matrix: ColorMatrix) {
    ORIGINAL(ColorMatrix().apply { setSaturation(1f) }),
    GRAYSCALE(ColorMatrix().apply { setSaturation(0f) }),
    SEPIA(ColorMatrix().apply { setSaturation(0f); setScale(1f, 0.95f, 0.82f, 1f) }),
    INVERT(ColorMatrix().apply { set(floatArrayOf(
        -1.0f, 0.0f, 0.0f, 0.0f, 255f,
        0.0f, -1.0f, 0.0f, 0.0f, 255f,
        0.0f, 0.0f, -1.0f, 0.0f, 255f,
        0.0f, 0.0f, 0.0f, 1.0f, 0f
    )) }),
    BRIGHTNESS(ColorMatrix().apply { set(floatArrayOf(
        1.2f, 0.0f, 0.0f, 0.0f, 0f,
        0.0f, 1.2f, 0.0f, 0.0f, 0f,
        0.0f, 0.0f, 1.2f, 0.0f, 0f,
        0.0f, 0.0f, 0.0f, 1.0f, 0f
    )) }),
    CONTRAST(ColorMatrix().apply { set(floatArrayOf(
        2.0f, 0.0f, 0.0f, 0.0f, -255f,
        0.0f, 2.0f, 0.0f, 0.0f, -255f,
        0.0f, 0.0f, 2.0f, 0.0f, -255f,
        0.0f, 0.0f, 0.0f, 1.0f, 0f
    )) });
}