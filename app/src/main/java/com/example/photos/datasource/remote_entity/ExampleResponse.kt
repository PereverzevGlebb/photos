package com.example.photos.datasource.remote_entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExampleResponse(
    @Json(name = "example") val example: String
)
