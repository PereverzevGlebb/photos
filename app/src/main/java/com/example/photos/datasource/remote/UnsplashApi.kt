package com.example.photos.datasource.remote

import com.example.photos.datasource.remote_entity.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val ACCESS_KEY = "DeVUwRu5BS9Z54ehK4QCJhIufGXXZ8JrupHA4f8i8gA"


interface UnsplashApi {
    @GET("photos")
    suspend fun getPhotos(
        @Query("client_id") clientId: String = ACCESS_KEY
    ): List<PhotoResponse>
}