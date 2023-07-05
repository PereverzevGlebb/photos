package com.example.photos.ui.dashboard

import com.example.photos.domain.entity.PhotoModel

sealed class UnsplashState {
    class Content(
        val data: List<PhotoModel>
    ) : UnsplashState()

    object Empty : UnsplashState()
    object Error : UnsplashState()
    object Loading : UnsplashState()

    companion object {
        fun fromContent(data: List<PhotoModel>): UnsplashState {
            return if (data.isEmpty()) Empty
            else Content(data)
        }
    }
}