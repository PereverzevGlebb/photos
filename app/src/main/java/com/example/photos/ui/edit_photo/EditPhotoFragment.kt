package com.example.photos.ui.edit_photo

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photos.R
import com.example.photos.base.ui.BaseFragment
import com.example.photos.databinding.FragmentEditPhotoBinding
import com.example.photos.utils.applyFilter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditPhotoFragment : BaseFragment<FragmentEditPhotoBinding>(
    FragmentEditPhotoBinding::inflate
) {

    private val args: EditPhotoFragmentArgs by navArgs()

    private val startedBitmap by lazy {
        decodeImageUri(args.selectedImageUri.toUri())
    }

    private var filteredBitmap: Bitmap? = null

    private var imageUriForSharing: Uri? = null

    private val filterAdapter by lazy {
        FilterAdapter {
            filteredBitmap = startedBitmap.applyFilter(it.matrix)
            binding.editPhotoImv.setImageBitmap(filteredBitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding.editPhotoImv.setImageBitmap(startedBitmap)
            setupMenu()
            setupFilterList()
    }

    private fun setupFilterList() = with(binding.rvFilters){
        adapter = filterAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupMenu() {
        binding.topToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_item -> {
                    filteredBitmap?.let { saveImageToGallery(it) }
                    true
                }

                R.id.share_item -> {
                    imageUriForSharing?.let { sharePhoto(it) }
                    true
                }

                else -> false
            }
        }
    }

    private fun sharePhoto(imageUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, imageUri)
        }
        startActivity(Intent.createChooser(shareIntent, "Share by"))
    }


    private fun decodeImageUri(uri: Uri): Bitmap {
        val input = requireActivity().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)
        input?.close()

        return bitmap
    }

    private fun applyBlackAndWhiteFilter(bitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return outputBitmap
    }

    private fun applyFilter(bitmap: Bitmap, colorMatrix: ColorMatrix): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return outputBitmap
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentResolver = requireActivity().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "filtered_image")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val imageUri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                Toast.makeText(requireContext(), "Downloaded", Toast.LENGTH_SHORT).show()
            }
        }
    }
}