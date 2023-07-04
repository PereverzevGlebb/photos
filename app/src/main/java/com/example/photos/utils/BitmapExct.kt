package com.example.photos.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.media.Image
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.TagBundle
import androidx.camera.core.impl.utils.ExifData
import java.nio.ByteBuffer

fun Bitmap.applyFilter(colorMatrix: ColorMatrix): Bitmap {
    val outputBitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(outputBitmap)
    val paint = Paint()
    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = colorFilter
    canvas.drawBitmap(this, 0f, 0f, paint)
    return outputBitmap
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.toImageProxy(): ImageProxy {
    val imagePlaneProxy = object : ImageProxy.PlaneProxy {
        override fun getBuffer(): ByteBuffer {
            val byteBuffer = ByteBuffer.allocateDirect(byteCount)
            copyPixelsToBuffer(byteBuffer)
            byteBuffer.rewind()
            return byteBuffer
        }

        override fun getRowStride(): Int {
            return rowBytes
        }

        override fun getPixelStride(): Int {
            return 4 // Assuming each pixel occupies 4 bytes (ARGB_8888 format)
        }
    }

    val imageProxy = @ExperimentalGetImage object : ImageProxy {
        override fun getWidth(): Int {
            return this@toImageProxy.width
        }

        override fun getHeight(): Int {
            return this@toImageProxy.height
        }

        override fun getFormat(): Int {
            return ImageFormat.YUV_420_888
        }

        override fun getPlanes(): Array<ImageProxy.PlaneProxy> {
            return arrayOf(imagePlaneProxy)
        }

        override fun getImageInfo(): ImageInfo {
            return object : ImageInfo {
                @SuppressLint("RestrictedApi")
                override fun getTagBundle(): TagBundle {
                    return TagBundle.emptyBundle()
                }

                override fun getTimestamp(): Long {
                    return System.currentTimeMillis()
                }

                override fun getRotationDegrees(): Int {
                    return 0
                }

                @SuppressLint("RestrictedApi")
                override fun populateExifData(exifBuilder: ExifData.Builder) {
                    exifBuilder.setImageHeight(height)
                    exifBuilder.setImageWidth(width)
                    exifBuilder.build()
                }

            }
        }

        override fun getCropRect(): Rect {
            return Rect(0, 0, width, height)
        }

        override fun setCropRect(rect: Rect?) {

        }

        override fun close() {
            // No-op
        }

        override fun getImage(): Image? {
            return null
        }
    }

    return imageProxy
}
