package com.example.photos.datasource.local

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.photos.base.data.transaction.entity.Transaction
import com.example.photos.base.data.transaction.flowWrap
import com.example.photos.base.data.transaction.transform
import com.example.photos.domain.entity.PhotoModel
import javax.inject.Inject

class ContentResolverDataSourceImpl @Inject constructor(
    private val contentResolver: ContentResolver
) : ContentResolverDataSource {

    companion object {
        private const val DIRECTORY_NAME = "EvaFolder"
    }

    override fun savePhotoToGallery(bitmap: Bitmap): String? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/$DIRECTORY_NAME"
            )
        }

        val imageUri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
        return imageUri?.toString()
    }

    override fun getPhotosFromDirectory() = Transaction.flowWrap {
        getAllImagesFromDirectory()
    }.transform { cursor ->
        val images = mutableListOf<PhotoModel>()
        cursor?.use {
            val idColumnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val imageId: Long = it.getLong(idColumnIndex)
                val imageUri: Uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toString()
                )
                images.add(
                    PhotoModel(
                        id = imageId.toString(),
                        urls = imageUri.toString()
                    )
                )
            }
        }
        images
    }


    private fun getAllImagesFromDirectory(): Cursor? {
        val selection =
            "${MediaStore.Files.FileColumns.DATA} LIKE '%${DIRECTORY_NAME}%'"
        val projection = arrayOf(MediaStore.Images.Media._ID)
        return contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )
    }
}
