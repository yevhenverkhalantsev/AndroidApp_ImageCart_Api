package com.example.artphoto.images.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.artphoto.images.model.*
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import com.example.artphoto.room.repository.ArtPhotoDatabase
import kotlinx.coroutines.*

class ImagesViewModel private constructor(
    private val repository: ImagesRepository,
    private val cartDatabase: ArtPhotoDatabase): ViewModel() {

    private val _photos = MutableLiveData<List<ArtPhoto>>()
    val photos get() = _photos
    var selectedPhoto: ArtPhoto? = null

    private val _selectedAlbum = MutableLiveData<ArtAlbum>()
    val selectedAlbum get() = _selectedAlbum

    private val _selectedAuthor = MutableLiveData<ArtArtist>()
    val selectedAAuthor get() = _selectedAuthor

    private val _cart = MutableLiveData<MutableList<CartPhoto>?>()
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
            _selectedAlbum.value = album.await()
            _selectedAuthor.postValue(repository.getArtist(album.await().userId))
        }
    }

    companion object {
        private var instance: ImagesViewModel? = null

        fun getInstance(repository: ImagesRepository, database: ArtPhotoDatabase): ImagesViewModel {
            if (instance == null) {
                instance = ImagesViewModel(repository, database)
            }
            return instance!!
        }
    }


    fun clearPhotos() {

        //CoroutineScope()
        viewModelScope.launch {
            cartDatabase.photoDao().deleteAllTables()
            _cart.value?.clear()
            //@TODO clear cart
        }
    }

    fun insertPhoto(cartPhoto: CartPhoto) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDatabase.photoDao().insertSize(cartPhoto.size)
        }
        CoroutineScope(Dispatchers.IO).launch {
            cartDatabase.photoDao().insertFrame(cartPhoto.frame)
        }
        CoroutineScope(Dispatchers.IO).launch {
            cartDatabase.photoDao().insertArtPhoto(cartPhoto.photo)
        }
        CoroutineScope(Dispatchers.IO).launch {
            cartDatabase.photoDao().insert(
                cartPhoto.cartPhotoDB
            )
        }
    }

    fun getAllCartPhotos() {
        CoroutineScope(Dispatchers.IO).launch {
            cart.postValue(cartDatabase.photoDao().getAllCartPhotos())

//            insertArtPhoto()
//            insertFrame()
//            insertSize()
        }
    }

    fun deleteSelectedPhotos(selectedCartPhotos: MutableList<CartPhoto>) {
        CoroutineScope(Dispatchers.IO).launch {
            val ids = mutableListOf<Int>()
            selectedCartPhotos.forEach { ids.add(it.cartPhotoDB.id) }
            cartDatabase.photoDao().deleteSelectedPhotos(ids)
            //_cart.postValue(cartDatabase.photoDao().getAllCartPhotos())
        }
    }

//    fun deleteByItem(cartPhotoDB: CartPhoto) {
//        viewModelScope.launch {
//            cartDatabase.photoDao().deleteById(cartPhotoDB.cartPhotoDB)
//        }
//    }



}