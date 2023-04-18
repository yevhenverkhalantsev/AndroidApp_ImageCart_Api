package com.example.artphoto.images.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.artphoto.R
import com.example.artphoto.context.MyApplication
import com.example.artphoto.databinding.FragmentImagesBinding
import com.example.artphoto.images.view.recyclerview.ImageRecyclerView
import com.example.artphoto.images.viewmodel.ImagesViewModel
import javax.inject.Inject

class ImagesFragment : Fragment() {
    @Inject lateinit var viewModel: ImagesViewModel
    private lateinit var recycler: ImageRecyclerView
    private var _binding : FragmentImagesBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        (requireContext().applicationContext as MyApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImagesBinding.bind(view)

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

    private fun initRecycler() {
        recycler = ImageRecyclerView {
            viewModel.selectedPhoto = it
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