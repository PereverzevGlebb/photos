package com.example.photos.datasource.remote_entity

import com.example.photos.domain.entity.PhotoModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoResponse (
    @Json(name = "id")
    val id: String,
    @Json(name = "urls")
    val urls: UrlsResponse,
    @Json(name = "links")
    val links: LinksResponse
)
@JsonClass(generateAdapter = true)
data class LinksResponse (
    @Json(name = "self")
    val self: String,
    @Json(name = "html")
    val html: String,
    @Json(name = "download")
    val download: String,
    @Json(name = "download_location")
    val downloadLocation: String
)
@JsonClass(generateAdapter = true)
data class UrlsResponse (
    @Json(name = "full")
    val full: String
)

fun PhotoResponse.mapToModel() = PhotoModel(
    id = this.id,
    urls = this.urls.full,
    links = this.links.download
)

