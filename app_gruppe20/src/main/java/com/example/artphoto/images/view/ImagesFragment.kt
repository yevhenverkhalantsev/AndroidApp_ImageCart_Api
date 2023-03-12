package com.example.artphoto.images.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.artphoto.R
import com.example.artphoto.databinding.FragmentImagesBinding
import com.example.artphoto.images.view.recyclerview.ImageRecyclerView
import com.example.artphoto.images.viewmodel.ImagesViewModel
import com.example.artphoto.images.viewmodel.repository.ArtPhotosApiService
import com.example.artphoto.images.viewmodel.repository.ImagesRepository

class ImagesFragment : Fragment() {
    private lateinit var viewModel: ImagesViewModel
    private lateinit var recycler: ImageRecyclerView
    private var _binding : FragmentImagesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImagesBinding.bind(view)


        initViewModel()
        initRecycler()
        getPhotos()
    }

    private fun getPhotos() {
        if (viewModel.photos.value == null) {
            viewModel.getPhotos()
        }
        viewModel.photos.observe(viewLifecycleOwner) {

            recycler.submitList(it)
        }
    }

    private fun initViewModel() {
        viewModel = ImagesViewModel.getInstance(ImagesRepository(ArtPhotosApiService.getInstance()))
    }

    private fun initRecycler() {
        recycler = ImageRecyclerView {
            viewModel.selectedPhoto = it
            Log.i("test", "viewModel BEFORE = ${viewModel}")
            Log.i("REST", "Selected photo: ${viewModel.selectedPhoto}")
            findNavController().navigate(R.id.action_imagesFragment_to_selectedImageFragment)
        }
        binding.recyclerView.adapter = recycler
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}