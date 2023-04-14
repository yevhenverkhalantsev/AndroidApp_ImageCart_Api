package com.example.artphoto.selected.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.databinding.FragmentSelectedImageBinding
import com.example.artphoto.images.model.CartPhoto
import com.example.artphoto.images.model.CartPhotoDB
import com.example.artphoto.images.model.Frame
import com.example.artphoto.images.model.Size
import com.example.artphoto.images.viewmodel.ImagesViewModel
import com.example.artphoto.images.viewmodel.repository.ArtPhotosApiService
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import com.example.artphoto.room.repository.ArtPhotoDatabase

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
        if (areOptionsSelected() && binding.amount.text.toString().toInt() > 0) {
            val frame = generateFrame()
            val size = generateSize()
            viewModel.insertPhoto(
                cartPhoto = CartPhoto(
                    photo = viewModel.selectedPhoto!!,
                    frame = frame,
                    size = size,
                    cartPhotoDB = CartPhotoDB(
                        price =  countPrice(frame, size),
                        artistName = viewModel.selectedAAuthor.value!!.name,
                        amount = binding.amount.text.toString().toInt())
                )
            )
            findNavController().navigate(R.id.action_selectedImageFragment_to_homeFragment)
        }
    }

    private fun countPrice(frame: Frame, size: Size): Int {
        return frame.price + size.price + 100
    }

    private fun generateSize(): Size {
        return when (binding.radioStRrelse.checkedRadioButtonId) {
            R.id.radioLite -> Size(name = "Lite", dimensions =  "20x20", price =  30)
            R.id.radioMedium -> Size(name = "Medium", dimensions =  "30x30", price =  80)
            R.id.radioStort -> Size(name =  "Stort", dimensions =  "50x50", price = 150)
            else -> Size(name = "0", dimensions =  "0", price =  0)
        }
    }


    private fun generateFrame(): Frame {
        return when (binding.radioRammer.checkedRadioButtonId) {
            R.id.radioTreramme -> Frame(name = "Treramme", price = 10)
            R.id.radioSølvramme -> Frame(name = "Sølvramme", price = 50)
            R.id.radioGullramme -> Frame(name = "Gullramme", price =  120)
            else -> Frame(name = "0", price =   0)
        }
    }

    private fun areOptionsSelected(): Boolean {
        if (binding.radioRammer.checkedRadioButtonId == -1) {
            Toast.makeText(requireContext(), getString(R.string.choose_frame_toast), Toast.LENGTH_SHORT).show()
        }
        else {
            if (binding.radioStRrelse.checkedRadioButtonId == -1) {
                Toast.makeText(requireContext(), getString(R.string.choose_size_toast), Toast.LENGTH_SHORT).show()
            }
            else if (binding.amount.text.toString().isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.choose_amount_toast), Toast.LENGTH_SHORT).show()
            }
            else if (binding.amount.text.toString().toInt() == 0) {
            Toast.makeText(requireContext(), getString(R.string.invalid_quantity_toast), Toast.LENGTH_SHORT).show()
            }

            else return true
        }
        return false
    }

    private fun initViewModel() {
        viewModel = ImagesViewModel.getInstance(ImagesRepository(ArtPhotosApiService.getInstance()),
            ArtPhotoDatabase.getDB(requireContext()))
        requestAlbumInfo()
    }

    private fun requestAlbumInfo() {
        if (viewModel.selectedPhoto == null) {
            //@TODO show error message
        }
        else {
            viewModel.getAlbumWithArtist(viewModel.selectedPhoto!!.albumId)
            viewModel.selectedAAuthor.observe(viewLifecycleOwner) {
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