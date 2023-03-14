package com.example.artphoto.images.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Frame(
    val name: String,
    val price: Int
)

data class Size(
    val name: String,
    val dimensions: String,
    val price: Int
)

data class CartPhoto(
    val photo: ArtPhoto,
    val frame: Frame,
    val size: Size,
    val price: Int,
    val artistName: String
)
@JsonClass(generateAdapter = true)
data class ArtPhoto(
    @Json(name = "albumId") val albumId: Int=-1,
    @Json(name = "id") val id: Int=-1,
    @Json(name = "title") val title: String="undefined",
    @Json(name = "url") val url: String="undefined",
    @Json(name = "thumbnailUrl") val thumbnailUrl: String="undefined",
)

@JsonClass(generateAdapter = true)
data class ArtArtist(
    @Json(name = "id") val id: Int=-1,
    @Json(name = "name") val name: String="undefined",
    @Json(name = "email") val email: String="undefined",
    @Json(name = "phone") val phone: String="undefined",
    @Json(name = "website") val website: String="undefined",
)

@JsonClass(generateAdapter = true)
data class ArtAlbum(
    @Json(name = "userId") val userId: Int = -1,
    @Json(name = "id") val id: Int = -1,
    @Json(name = "title") val title: String = "undefined",
)