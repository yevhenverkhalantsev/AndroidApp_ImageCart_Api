package com.example.artphoto.images.viewmodel

import androidx.lifecycle.*
import com.example.artphoto.images.model.ArtPhoto
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import kotlinx.coroutines.launch

class ImagesViewModel(private val repository: ImagesRepository): ViewModel() {
    private val _photos = MutableLiveData<List<ArtPhoto>>()
    val photos: LiveData<List<ArtPhoto>> = _photos
    var selectedPhoto: ArtPhoto? = null

    fun getPhotos() {
        viewModelScope.launch {
            _photos.postValue(repository.getPhotos()) }
    }
//    fun getAlbum(id: Int) = repository.getAlbum(id)
//    fun getArtist(id: Int) = repository.getArtist(id)

}
class MyImagesViewModelFactory(private val repository: ImagesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImagesViewModel(repository) as T
    }
}