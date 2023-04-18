package com.example.artphoto.images.viewmodel

import androidx.lifecycle.*
import com.example.artphoto.images.model.*
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import com.example.artphoto.room.repository.ArtPhotoDatabase
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesViewModel @Inject constructor(
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

    init {
        getAllCartPhotos()
        getPhotos()
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

    fun clearPhotos() {
        viewModelScope.launch {
            cartDatabase.photoDao().deleteAllTables()
            _cart.value?.clear()
        }
    }

    fun insertPhoto(cartPhoto: CartPhoto) {
        CoroutineScope(Dispatchers.IO).launch{
            val insertSize = GlobalScope.async { cartDatabase.photoDao().insertSize(cartPhoto.size) }
            val insertFrame = GlobalScope.async { cartDatabase.photoDao().insertFrame(cartPhoto.frame) }
            val insertArtPhoto = GlobalScope.async { cartDatabase.photoDao().insertArtPhoto(cartPhoto.photo) }
            val insertCartPhoto = GlobalScope.async { cartDatabase.photoDao().insert(cartPhoto.cartPhotoDB) }

            withContext(Dispatchers.Main) {
                insertSize.await()
                insertFrame.await()
                insertArtPhoto.await()
                insertCartPhoto.await()
                getAllCartPhotos()
            }
        }

    }

    fun getAllCartPhotos() {
        CoroutineScope(Dispatchers.IO).launch {
            cart.postValue(cartDatabase.photoDao().getAllCartPhotos())
        }
    }

    fun deleteSelectedPhotos(selectedCartPhotos: MutableList<CartPhoto>) {
        CoroutineScope(Dispatchers.IO).launch {
            val ids = mutableListOf<Int>()
            selectedCartPhotos.forEach { ids.add(it.cartPhotoDB.id) }
            cartDatabase.photoDao().deleteSelectedPhotos(ids)
        }
    }





}