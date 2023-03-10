package com.example.artphoto.images.viewmodel.repository

import com.example.artphoto.images.model.ArtAlbum
import com.example.artphoto.images.model.ArtArtist
import com.example.artphoto.images.model.ArtPhoto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

        fun getInstance(): ArtPhotosApiService { //
            if (retrofit == null) {
                val logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(logLevel)
                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(client)
                    .build()
                    .create(ArtPhotosApiService::class.java)
            }
            return retrofit!!
        }

    }

}