package com.example.photos.ui.photo_feed

import com.example.photos.domain.entity.PhotoModel

sealed class PhotoFeedState {
    class Content(
        val data: List<PhotoModel>
    ) : PhotoFeedState()

    object Empty : PhotoFeedState()
    object Error : PhotoFeedState()
    object Loading : PhotoFeedState()

    companion object {
        fun fromContent(data: List<PhotoModel>): PhotoFeedState {
            return if (data.isEmpty()) Empty
            else Content(data)
        }
    }
}