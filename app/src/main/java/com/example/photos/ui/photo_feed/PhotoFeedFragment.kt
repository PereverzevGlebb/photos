package com.example.photos.ui.photo_feed

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photos.R
import com.example.photos.base.ui.BaseFragment
import com.example.photos.base.ui.collect
import com.example.photos.base.ui.sharePhoto
import com.example.photos.databinding.FragmentPhotoFeedBinding
import com.example.photos.ui.common.PhotoFeedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFeedFragment : BaseFragment<FragmentPhotoFeedBinding>(
    FragmentPhotoFeedBinding::inflate
) {

    private val viewModel: PhotoFeedViewModel by viewModels()

    private var selectedImageUri: Uri? = null

    private var isAllFabsVisible = false

    private val permissionResultLauncherForImagePicker =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (!result.containsValue(false))
                openPhotoPicker()
            else
                Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }

    private val permissionResultLauncherForGettingPhotos =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (!result.containsValue(false))
                viewModel.refresh()
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

    private val photoFeedAdapter by lazy {
        PhotoFeedAdapter(
            onPhotoClick = { photo ->
                findNavController().navigate(
                    PhotoFeedFragmentDirections.actionOpenSelectedPhoto(
                        photo.urls
                    )
                )
            },
            onShareClick = { photo ->
                sharePhoto(photo.urls.toUri())
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        setupRecyclerView()
        hideActionButtons()
        setupClickListeners()
        getSavedPhotosFromGallery()
    }

    private fun setupObserver() {
        collect(viewModel.photoGalleryState) { photos ->
            onStateChanged(photos)
        }
    }

    private fun onStateChanged(state: PhotoFeedState) {
        when (state) {
            is PhotoFeedState.Content -> bindContent(state)
            PhotoFeedState.Empty -> bindEmpty()
            PhotoFeedState.Error -> bindError()
            PhotoFeedState.Loading -> bindLoading()
        }
    }

    private fun bindContent(state: PhotoFeedState.Content) = with(binding) {
        errorFeedText.isVisible = false
        emptyFeedText.isVisible = false
        photoFeedProgress.isVisible = false
        rvPhotoFeed.isVisible = true
        photoFeedAdapter.submitList(state.data)
    }

    private fun bindLoading() = with(binding) {
        errorFeedText.isVisible = false
        emptyFeedText.isVisible = false
        photoFeedProgress.isVisible = true
        rvPhotoFeed.isVisible = false
    }

    private fun bindError() = with(binding) {
        errorFeedText.isVisible = true
        emptyFeedText.isVisible = false
        photoFeedProgress.isVisible = false
        rvPhotoFeed.isVisible = false
    }

    private fun bindEmpty() = with(binding) {
        errorFeedText.isVisible = false
        emptyFeedText.isVisible = true
        photoFeedProgress.isVisible = false
        rvPhotoFeed.isVisible = false
    }

    private fun checkActionButtons() {
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

    private fun setupClickListeners() = with(binding) {
        openFab.setOnClickListener {
            checkActionButtons()
        }
        makeNewPhotoFab.setOnClickListener {
            findNavController().navigate(R.id.cameraFragment)
        }
        photoFromGalleryFab.setOnClickListener {
            checkReadExternalStoragePermissions()
        }
        photoFromUnsplashFab.setOnClickListener {
            findNavController().navigate(R.id.unsplashFragment)
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvPhotoFeed.adapter = photoFeedAdapter
        rvPhotoFeed.layoutManager = GridLayoutManager(context, 2)
    }

    private fun openPhotoPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun getSavedPhotosFromGallery() {
        if (allPermissionsGranted())
            viewModel.refresh()
        else
            permissionResultLauncherForGettingPhotos.launch(REQUIRED_PERMISSIONS)

    }

    private fun checkReadExternalStoragePermissions() {
        if (allPermissionsGranted()) {
            openPhotoPicker()
        } else {
            permissionResultLauncherForImagePicker.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            else arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
    }
}