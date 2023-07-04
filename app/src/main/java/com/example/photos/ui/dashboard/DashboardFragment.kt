package com.example.photos.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.photos.base.ui.BaseFragment
import com.example.photos.base.ui.collect
import com.example.photos.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(
    FragmentDashboardBinding::inflate
) {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() = with(viewModel) {
        collect(state) {
            onStateChanged(it)
        }
    }

    private fun onStateChanged(state: ExampleState) =
        when(state) {
            is ExampleState.Content -> bindContent(state)
            is ExampleState.ErrorState -> bindError(state)
            ExampleState.LoadingState -> bindLoading()
        }

    private fun bindContent(state: ExampleState.Content) = with(binding) {
        textDashboard.text = state.data
    }

    private fun bindError(state: ExampleState.ErrorState) {
        binding.textDashboard.text = state.error.name
    }

    private fun bindLoading() {
        binding.textDashboard.text = "loading"
    }
}

