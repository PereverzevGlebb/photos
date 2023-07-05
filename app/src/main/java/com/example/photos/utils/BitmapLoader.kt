package com.example.photos.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BitmapLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun downloadBitmap(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
            } catch (e: Exception) {
                null
            }
        }
    }
}