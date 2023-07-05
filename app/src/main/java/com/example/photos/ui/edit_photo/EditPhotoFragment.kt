package com.example.photos.ui.edit_photo

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.photos.R
import com.example.photos.base.ui.BaseFragment
import com.example.photos.databinding.FragmentEditPhotoBinding
import com.example.photos.ui.common.FilterAdapter
import com.example.photos.utils.BitmapLoader
import com.example.photos.utils.applyFilter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class EditPhotoFragment : BaseFragment<FragmentEditPhotoBinding>(
    FragmentEditPhotoBinding::inflate
) {

    private val viewModel: EditPhotoViewModel by viewModels()

    @Inject
    lateinit var bitmapLoader: BitmapLoader

    private val args: EditPhotoFragmentArgs by navArgs()

    private var startedBitmap: Bitmap? = null

    private var filteredBitmap: Bitmap? = null

    private var imageUriForSharing: Uri? = null

    private val filterAdapter by lazy {
        FilterAdapter {
            filteredBitmap = startedBitmap?.applyFilter(it.matrix)
            setupBitmap(filteredBitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupFilterList()
        loadBitmap()
    }

    private fun loadBitmap() {
        viewLifecycleOwner.lifecycleScope.launch {
            startedBitmap = bitmapLoader.downloadBitmap(args.selectedImageUri)
            setupBitmap(startedBitmap)
        }
    }

    private fun setupFilterList() = with(binding.rvFilters) {
        adapter = filterAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupMenu() {
        binding.topToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_item -> {
                    filteredBitmap?.let { viewModel.savePhotoToGallery(it) }
                    true
                }

                else -> false
            }
        }
    }

    private fun setupBitmap(bitmap: Bitmap?) {
        Glide.with(requireContext())
            .load(bitmap)
            .into(binding.editPhotoImv)
    }
}