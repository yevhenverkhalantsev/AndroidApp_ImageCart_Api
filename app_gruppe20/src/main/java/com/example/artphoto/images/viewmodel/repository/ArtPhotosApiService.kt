package com.example.artphoto.images.viewmodel.repository

import com.example.artphoto.images.model.ArtAlbum
import com.example.artphoto.images.model.ArtArtist
import com.example.artphoto.images.model.ArtPhoto
import retrofit2.http.GET
import retrofit2.http.Path


interface ArtPhotosApiService {

    @GET("photos")
    suspend fun getPhotos(): List<ArtPhoto>

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") id: Int): ArtAlbum

    @GET("users/{id}")
    suspend fun getArtist(@Path("id") id: Int): ArtArtist

    companion object {
        const val BASE_URL: String = "https://jsonplaceholder.typicode.com"
    }

}