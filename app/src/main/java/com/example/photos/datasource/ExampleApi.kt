package com.example.photos.datasource

import com.example.photos.datasource.remote_entity.ExampleResponse
import retrofit2.http.GET

interface ExampleApi {

    @GET(value = "example")
    suspend fun getExample(): ExampleResponse
}