package com.example.artphoto.images.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.artphoto.R
import com.example.artphoto.images.model.*
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import kotlinx.coroutines.*

class ImagesViewModel private constructor(private val repository: ImagesRepository): ViewModel() {
    private val _photos = MutableLiveData<List<ArtPhoto>>()
    val photos get() = _photos
    var selectedPhoto: ArtPhoto? = null

    private val _selectedAlbum = MutableLiveData<ArtAlbum>()
    val selectedAlbum get() = _selectedAlbum

    private val _selectedAuthor = MutableLiveData<ArtArtist>()
    val selectedAAuthor get() = _selectedAuthor

    private val _cart = MutableLiveData<MutableList<CartPhoto>>()
    val cart get() = _cart

    fun resetCart() {
        _cart.value?.clear()

    }


    fun getPhotos() {
        viewModelScope.launch {
            _photos.postValue(repository.getPhotos()) }
    }
    fun getAlbumWithArtist(albumId: Int) {
        viewModelScope.launch {
            val album = GlobalScope.async { repository.getAlbum(albumId) }
            Log.i("REST", "album: ${album.await()}")
            _selectedAlbum.value = album.await()
            _selectedAuthor.postValue(repository.getArtist(album.await().userId))
        }
    }

    fun addToCart(frame: Frame, size: Size) {
        viewModelScope.launch {
            if (_cart.value == null) {
                _cart.value = mutableListOf()
                Log.i("test", "Cart was empty but now no!")
            }
            _cart.value!!.add(CartPhoto(selectedPhoto!!,
                frame,
                size,
                countPrice(frame, size),
                artistName = selectedAAuthor.value!!.name))
            Log.i("test", "Cart: ${_cart.value}")
        }

    }

    private fun countPrice(frame: Frame, size: Size): Int {

        return frame.price + size.price + 100
    }

    companion object {
        private var instance: ImagesViewModel? = null

        fun getInstance(repository: ImagesRepository): ImagesViewModel {
            if (instance == null) {
                instance = ImagesViewModel(repository)
            }
            return instance!!
        }
    }


}