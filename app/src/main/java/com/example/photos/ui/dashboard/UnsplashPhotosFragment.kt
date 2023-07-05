package com.example.photos.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photos.base.ui.BaseFragment
import com.example.photos.base.ui.collect
import com.example.photos.base.ui.shareLink
import com.example.photos.databinding.FragmentUnsplashFeedBinding
import com.example.photos.ui.common.PhotoFeedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnsplashPhotosFragment : BaseFragment<FragmentUnsplashFeedBinding>(
    FragmentUnsplashFeedBinding::inflate
) {

    private val viewModel: UnsplashPhotosViewModel by viewModels()

    private val unsplashAdapter: PhotoFeedAdapter by lazy {
        PhotoFeedAdapter(
            onPhotoClick = {
                findNavController().navigate(
                    UnsplashPhotosFragmentDirections.actionUnsplashFragmentToEditPhotoFragment(
                        it.urls
                    )
                )
            },
            onShareClick = { photo ->
                shareLink(photo.urls)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() = with(binding.rvUnsplash) {
        adapter = unsplashAdapter
        layoutManager = GridLayoutManager(context, 2)
    }

    private fun setupObservers() = with(viewModel) {
        collect(unsplashState) { state ->
            onStateChanged(state)
        }
    }

    private fun onStateChanged(state: UnsplashState) {
        when (state) {
            is UnsplashState.Content -> bindContent(state)
            is UnsplashState.Empty -> bindEmptyState()
            is UnsplashState.Error -> bindError()
            is UnsplashState.Loading -> bindLoading()
        }
    }

    private fun bindEmptyState() = with(binding) {
        emptyText.isVisible = true
        errorText.isVisible = false
        rvUnsplash.isVisible = false
        unsplashProgress.isVisible = false
    }

    private fun bindContent(data: UnsplashState.Content) = with(binding) {
        emptyText.isVisible = false
        errorText.isVisible = false
        rvUnsplash.isVisible = true
        unsplashProgress.isVisible = false

        unsplashAdapter.submitList(data.data)

    }

    private fun bindLoading() = with(binding) {
        emptyText.isVisible = false
        errorText.isVisible = false
        rvUnsplash.isVisible = false
        unsplashProgress.isVisible = true
    }

    private fun bindError() = with(binding) {
        emptyText.isVisible = false
        errorText.isVisible = true
        rvUnsplash.isVisible = false
        unsplashProgress.isVisible = false
    }

}

