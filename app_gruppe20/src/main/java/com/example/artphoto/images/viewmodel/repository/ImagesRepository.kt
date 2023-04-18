package com.example.artphoto.images.viewmodel.repository

import javax.inject.Inject

class ImagesRepository @Inject constructor(private val apiService: ArtPhotosApiService) {
    suspend fun getPhotos() = apiService.getPhotos()
    suspend fun getAlbum(id: Int) = apiService.getAlbum(id)
    suspend fun getArtist(id: Int) = apiService.getArtist(id)
}
