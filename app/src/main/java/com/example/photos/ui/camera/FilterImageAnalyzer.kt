package com.example.photos.ui.camera

import android.widget.ImageView
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.photos.ui.edit_photo.FilterMatrix
import com.example.photos.utils.applyFilter
import com.example.photos.utils.rotate

class FilterImageAnalyzer(
    private val preview: ImageView
) : ImageAnalysis.Analyzer {

    var currentFilter = FilterMatrix.ORIGINAL

    override fun analyze(image: ImageProxy) {
        val bitmap = image
            .toBitmap()
            .rotate(90f)
            .applyFilter(currentFilter.matrix)
        preview.post {
            preview.setImageBitmap(bitmap)
            image.close()
        }
    }

}