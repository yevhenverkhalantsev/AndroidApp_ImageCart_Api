package com.example.artphoto.selected.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.databinding.FragmentSelectedImageBinding
import com.example.artphoto.images.viewmodel.ImagesViewModel
import com.example.artphoto.images.viewmodel.MyImagesViewModelFactory
import com.example.artphoto.images.viewmodel.repository.ArtPhotosApiService
import com.example.artphoto.images.viewmodel.repository.ImagesRepository

class SelectedImageFragment : Fragment() {
    private var _binding: FragmentSelectedImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectedImageBinding.bind(view)

        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, MyImagesViewModelFactory(
            ImagesRepository(ArtPhotosApiService.getInstance())))[ImagesViewModel::class.java]
        //val myFactory = MyImagesViewModelFactory(ImagesRepository(ArtPhotosApiService.getInstance()))
        //viewModel = myFactory.create(ImagesViewModel::class.java)
        Log.i("result", "viewModel: ${viewModel}")
        Log.i("result", "viewModel.firstSmthing: ${viewModel.photos.value?.get(0)}")
        Log.i("result", "viewModel.selectedPhoto: ${viewModel.selectedPhoto}")
        requestAlbumInfo()
    }

    private fun requestAlbumInfo() {
        if (viewModel.selectedPhoto == null) {
            //@TODO show error message
        }
        else {
            viewModel.getAlbumWithArtist(viewModel.selectedPhoto!!.albumId)
            viewModel.selectedAAuthor.observe(viewLifecycleOwner) {
                Log.i("REST", "Artist = ${it}")
                binding.authorName.text = it.name
                binding.authorMail.text = it.email
                Glide.with(requireContext())
                    .load(viewModel.selectedPhoto!!.thumbnailUrl)
                    .into(binding.image)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}