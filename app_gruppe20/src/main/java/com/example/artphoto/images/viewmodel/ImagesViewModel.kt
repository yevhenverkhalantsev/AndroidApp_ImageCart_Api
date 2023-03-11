package com.example.artphoto.images.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.artphoto.images.model.ArtAlbum
import com.example.artphoto.images.model.ArtArtist
import com.example.artphoto.images.model.ArtPhoto
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import kotlinx.coroutines.*

class ImagesViewModel(private val repository: ImagesRepository): ViewModel() {
    private val _photos = MutableLiveData<List<ArtPhoto>>()
    val photos get() = _photos
    var selectedPhoto: ArtPhoto? = null

    private val _selectedAlbum = MutableLiveData<ArtAlbum>()
    val selectedAlbum get() = _selectedAlbum

    private val _selectedAuthor = MutableLiveData<ArtArtist>()
    val selectedAAuthor get() = _selectedAuthor


    fun getPhotos() {
        viewModelScope.launch {
            _photos.postValue(repository.getPhotos()) }
    }

    fun getAlbum(id: Int) {
        viewModelScope.launch {
            _selectedAlbum.postValue(repository.getAlbum(id))
            // Do something with album, such as post it to a LiveData variable
        }
    }

    fun getArtist(id: Int) {
        viewModelScope.launch {
            _selectedAuthor.postValue(repository.getArtist(id))
            // Do something with artist, such as post it to a LiveData variable
        }
    }

    fun getAlbumWithArtist(albumId: Int) {
        viewModelScope.launch {
            val album = GlobalScope.async { repository.getAlbum(albumId) }
            Log.i("REST", "album: ${album.await()}")
            _selectedAlbum.value = album.await()
            _selectedAuthor.postValue(repository.getArtist(album.await().userId))
        }
    }


}
class MyImagesViewModelFactory(private val repository: ImagesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ImagesViewModel::class.java)) {
            ImagesViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}