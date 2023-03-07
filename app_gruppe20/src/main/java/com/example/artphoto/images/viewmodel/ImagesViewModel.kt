package com.example.artphoto.images.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphoto.images.model.ArtPhoto
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import kotlinx.coroutines.launch

class ImagesViewModel(private val repository: ImagesRepository): ViewModel() {  // class for å hente data fra API og sende til view
    private val _photos: LiveData<List<ArtPhoto>> = MutableLiveData()

    fun getPhotos() {
        viewModelScope.launch {
            _photos. repository.getPhotos() }
    }
    fun getAlbum(id: Int) = repository.getAlbum(id)
    fun getArtist(id: Int) = repository.getArtist(id)

}