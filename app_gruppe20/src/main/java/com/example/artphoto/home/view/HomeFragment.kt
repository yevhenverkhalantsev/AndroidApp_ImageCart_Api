package com.example.artphoto.home.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.artphoto.R
import com.example.artphoto.databinding.FragmentHomeBinding
import com.example.artphoto.home.view.recycleradapter.HomeRecyclerView
import com.example.artphoto.images.viewmodel.ImagesViewModel
import com.example.artphoto.images.viewmodel.repository.ArtPhotosApiService
import com.example.artphoto.images.viewmodel.repository.ImagesRepository

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ImagesViewModel

    private lateinit var recyclerAdapter: HomeRecyclerView

    private fun initViewModel() {
        viewModel = ImagesViewModel.getInstance(ImagesRepository(ArtPhotosApiService.getInstance()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)


        binding.chooseImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_imagesFragment)
        }

        binding.checkoutButton.setOnClickListener {
            if (!cartIsEmpty()) sendBuyingMessage()
            else {
                Toast.makeText(requireContext(), "Vennligst velg bilder!", Toast.LENGTH_SHORT).show()
            }
        }

        initViewModel()
        initRecycler()
        setRecyclerData()
    }

    private fun cartIsEmpty(): Boolean {
        if (viewModel.cart.value == null || viewModel.cart.value?.size == 0) {
            return true
        }
        return false
    }

    private fun sendBuyingMessage() {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:" + "yve001@uit.no")
                putExtra(Intent.EXTRA_SUBJECT, "Ordreinformasjon")
                putExtra(Intent.EXTRA_TEXT, generateMessageBody())
            }
        startActivity(intent)
        reset()

    }

    private fun reset() {
        viewModel.resetCart()
        recyclerAdapter.artPhoto = emptyList()
        binding.TotalPrice.text = getString(R.string.total_price, 0)
        binding.AmountImages.text = getString(R.string.amountImages, 0)
        Toast.makeText(requireContext(), "Takk for din bestilling!", Toast.LENGTH_SHORT).show()
        Log.i("test", "RESET")
    }


    private fun generateMessageBody(): String {
        var imagesIndex = "\nBildeindekser:\n"
        var message = "Kunstner:\n"

        viewModel.cart.value!!.forEachIndexed { index, item ->
            imagesIndex += item.photo.id.toString()
            message += item.artistName
            if (index < viewModel.cart.value!!.size - 1) {
                imagesIndex += ", "
                message += ", "
            }
            else {
                imagesIndex += ". "
                message += ". "
            }
        }
        return message + imagesIndex + "\nTotalpris:\n" + countTotalPrice().toString() + "."
    }

    private fun setRecyclerData() {
        viewModel.cart.observe(viewLifecycleOwner) {
            recyclerAdapter.artPhoto = it
            binding.TotalPrice.text = getString(R.string.total_price, countTotalPrice())
            binding.AmountImages.text = getString(R.string.amountImages, it.size)
        }
    }

    private fun countTotalPrice(): Int {
        var price = 0
        viewModel.cart.value!!.forEach {
            price += it.price
        }
        return price
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        recyclerAdapter = HomeRecyclerView()
        binding.chosenImagesRecyclerView.adapter = recyclerAdapter
        binding.chosenImagesRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }


}


