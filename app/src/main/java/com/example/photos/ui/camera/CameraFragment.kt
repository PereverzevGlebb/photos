package com.example.photos.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photos.base.ui.BaseFragment
import com.example.photos.databinding.FragmentCameraBinding
import com.example.photos.ui.edit_photo.FilterAdapter
import com.example.photos.utils.applyFilter
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.properties.Delegates

@AndroidEntryPoint
class CameraFragment : BaseFragment<FragmentCameraBinding>(
    FragmentCameraBinding::inflate
) {

    private var photoUri: Uri? = null
    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA

    private var analyzerExecutor: ExecutorService by Delegates.notNull()

    private var filterImageAnalyzer: FilterImageAnalyzer by Delegates.notNull()

    private val filterAdapter by lazy {
        FilterAdapter {
            filterImageAnalyzer.currentFilter = it
        }
    }

    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (!result.containsValue(false))
                startCamera()
            else
                Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyzerExecutor = Executors.newSingleThreadExecutor()
        filterImageAnalyzer = FilterImageAnalyzer(binding.viewFinder)
        checkCameraPermissions()
        setupClickListeners()
        setupFilterList()
    }

    override fun onDestroyView() {
        analyzerExecutor.shutdown()
        super.onDestroyView()
    }
    private fun setupFilterList() = with(binding.rvFilters) {
        adapter = filterAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupClickListeners() = with(binding) {
        cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
        cameraSwitchButton.setOnClickListener {
            switchLens()
        }
    }

    private fun checkCameraPermissions() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            permissionResultLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    savePhotoToGallery(image)
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        val imageAnalysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
        imageAnalysis.setAnalyzer(analyzerExecutor, filterImageAnalyzer)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = lensFacing

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, imageCapture, imageAnalysis
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun switchLens() {
        lensFacing = if (CameraSelector.DEFAULT_FRONT_CAMERA == lensFacing) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        startCamera()
    }


    private fun savePhotoToGallery(image: ImageProxy) {
        val bitmap = image.toBitmap()
            .applyFilter(filterImageAnalyzer.currentFilter.matrix)

        val displayName = "photo_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }

        val resolver = requireActivity().contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        photoUri = uri

        uri?.let { photoUri ->
            try {
                val outputStream = resolver.openOutputStream(photoUri)
                outputStream?.use { photoOutputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, photoOutputStream)
                    photoOutputStream.flush()
                }
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        findNavController().navigate(
            CameraFragmentDirections.actionCameraFragmentToEditPhotoFragment(uri.toString())
        )

        bitmap.recycle()
    }

    companion object {
        private const val TAG = "CameraX"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}