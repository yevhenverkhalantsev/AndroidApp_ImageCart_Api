package com.example.artphoto.selected.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.databinding.FragmentSelectedImageBinding
import com.example.artphoto.images.model.Frame
import com.example.artphoto.images.model.Size
import com.example.artphoto.images.viewmodel.ImagesViewModel
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
        initListeners()
    }

    private fun initListeners() {
        binding.GoBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectedImageFragment_to_imagesFragment)
        }

        binding.AddToCartButton.setOnClickListener {
            saveSelectedImageToCart()
            //viewModel.addToCart(viewModel.selectedPhoto!!)
        }
    }

    private fun saveSelectedImageToCart() {
        if (areOptionsSelected()) {
            viewModel.addToCart(
                frame = generateFrame(),
                size = generateSize()
            )
            findNavController().navigate(R.id.action_selectedImageFragment_to_homeFragment)
        }
    }

    private fun generateSize(): Size {
        when (binding.radioStRrelse.checkedRadioButtonId) {
            R.id.radioLite -> return Size("Lite", "20x20", 30)
            R.id.radioMedium -> return Size("Medium", "30x30", 80)
            R.id.radioStort -> return Size("Stort", "30x30", 150)
    }


    private fun generateFrame(): Frame {
        when (binding.radioRammer.checkedRadioButtonId) {
            R.id.radioTreramme -> return Frame("Treramme",  10)
            R.id.radioSølvramme -> return Frame("Sølvramme",  50)
            R.id.radioGullramme -> return Frame("Gullramme",  120)
    }
    }

    private fun areOptionsSelected(): Boolean {
        if (binding.radioRammer.checkedRadioButtonId == -1) {
            Toast.makeText(requireContext(), "Please select a frame", Toast.LENGTH_SHORT).show()
        }
        else {
            if (binding.radioStRrelse.checkedRadioButtonId == -1) {
                Toast.makeText(requireContext(), "Please select a size", Toast.LENGTH_SHORT).show()
            }
            else return true
        }
        return false
    }

    private fun initViewModel() {
        viewModel = ImagesViewModel.getInstance(ImagesRepository(ArtPhotosApiService.getInstance()))
        //val myFactory = MyImagesViewModelFactory(ImagesRepository(ArtPhotosApiService.getInstance()))
        //viewModel = myFactory.create(ImagesViewModel::class.java)
        requestAlbumInfo()
    }

    private fun requestAlbumInfo() {
        if (viewModel.selectedPhoto == null) {
            //@TODO show error message
        }
        else {
            Log.i("TEST", "Selected photo: ${viewModel.selectedPhoto}")
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