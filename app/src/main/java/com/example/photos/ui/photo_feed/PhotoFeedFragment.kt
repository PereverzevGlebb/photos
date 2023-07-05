package com.example.photos.ui.photo_feed

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photos.R
import com.example.photos.base.ui.BaseFragment
import com.example.photos.databinding.FragmentPhotoFeedBinding
import com.example.photos.ui.dashboard.UnsplashFeedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFeedFragment : BaseFragment<FragmentPhotoFeedBinding>(
    FragmentPhotoFeedBinding::inflate
) {

    private var selectedImageUri: Uri? = null

    private var isAllFabsVisible = false

    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (!result.containsValue(false))
                openPhotoPicker()
            else
                Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                val action =
                    PhotoFeedFragmentDirections.actionOpenSelectedPhoto(selectedImageUri = selectedImageUri.toString())
                findNavController().navigate(action)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val unsplashFeedAdapter by lazy {
        UnsplashFeedAdapter{}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun openFloatingPhotoActions() = with(binding) {
        openFab.setOnClickListener {
            checkActionButtons()
        }
    }

    private fun checkActionButtons() = with(binding) {
        if (!isAllFabsVisible) {
            showActionButtons()
        } else {
            hideActionButtons()
        }
    }

    private fun showActionButtons() = with(binding) {
        makeNewPhotoFab.show()
        photoFromGalleryFab.show()
        photoFromUnsplashFab.show()
        actionsButtonGroup.isVisible = true

        isAllFabsVisible = true
    }

    private fun hideActionButtons() = with(binding) {
        makeNewPhotoFab.hide()
        photoFromGalleryFab.hide()
        photoFromUnsplashFab.hide()
        actionsButtonGroup.isVisible = false

        isAllFabsVisible = false
    }

    private fun setupUI() {
        setupRecyclerView()
        hideActionButtons()
        setupMakePhotoButton()
        setupGetPhotoFromGalleryButton()
        navigateToUnsplashFeedFragment()
        openFloatingPhotoActions()
    }

    private fun setupMakePhotoButton() = with(binding) {
        makePhotoActionText.setOnClickListener {
            findNavController().navigate(R.id.cameraFragment)
        }
    }

    private fun setupGetPhotoFromGalleryButton() = with(binding) {
        photoFromGalleryFab.setOnClickListener {
            checkReadExternalStoragePermissions()
        }
    }

    private fun navigateToUnsplashFeedFragment() = with(binding) {
        photoFromUnsplashFab.setOnClickListener {
            findNavController().navigate(R.id.unsplashFragment)
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvPhotoFeed.adapter = unsplashFeedAdapter
        rvPhotoFeed.layoutManager = GridLayoutManager(context, 2)
    }

    private fun openPhotoPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun checkReadExternalStoragePermissions() {
        if (allPermissionsGranted()) {
            openPhotoPicker()
        } else {
            permissionResultLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }


    companion object {

        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).toTypedArray()
    }
}