package com.example.artphoto.images.viewmodel.repository

import com.example.artphoto.images.model.ArtAlbum
import com.example.artphoto.images.model.ArtArtist
import com.example.artphoto.images.model.ArtPhoto
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
        var retrofit: ArtPhotosApiService? = null
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun getInstance(): ArtPhotosApiService {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(ArtPhotosApiService::class.java)
            }
            return retrofit!!
        }

    }

}