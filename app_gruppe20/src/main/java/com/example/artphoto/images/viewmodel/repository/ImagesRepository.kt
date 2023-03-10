package com.example.artphoto.images.viewmodel.repository

class ImagesRepository(private val apiService: ArtPhotosApiService) {
    suspend fun getPhotos() = apiService.getPhotos()
    suspend fun getAlbum(id: Int) = apiService.getAlbum(id)
    suspend fun getArtist(id: Int) = apiService.getArtist(id)
}
