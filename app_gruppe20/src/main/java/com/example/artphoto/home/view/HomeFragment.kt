package com.example.artphoto.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.artphoto.R
import com.example.artphoto.databinding.FragmentHomeBinding
import com.example.artphoto.home.view.recycleradapter.HomeRecyclerView
import com.example.artphoto.images.model.CartPhoto
import com.example.artphoto.images.viewmodel.ImagesViewModel
import com.example.artphoto.images.viewmodel.repository.ArtPhotosApiService
import com.example.artphoto.images.viewmodel.repository.ImagesRepository
import com.example.artphoto.room.repository.ArtPhotoDatabase


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ImagesViewModel
    private lateinit var recyclerAdapter: HomeRecyclerView
    private var madeDelete: Boolean = false
    private fun initViewModel() {
        viewModel = ImagesViewModel.getInstance(ImagesRepository(ArtPhotosApiService.getInstance()),
            ArtPhotoDatabase.getDB(requireContext()))
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

        binding.resetButton.setOnClickListener {
            viewModel.clearPhotos()
            recyclerAdapter.artPhoto = mutableListOf()
            binding.TotalPrice.text = getString(R.string.total_price, 0)
            binding.AmountImages.text = getString(R.string.amountImages, 0)
        }

        binding.deleteButton.setOnClickListener {
            val tempCartPhotos = recyclerAdapter.selectedCartPhotos.toMutableList()
            viewModel.deleteSelectedPhotos(tempCartPhotos)
            recyclerAdapter.deleteSelectedItems()
            binding.TotalPrice.text = getString(R.string.total_price, countTotalPrice(
                recyclerAdapter.artPhoto))
            binding.AmountImages.text = getString(R.string.amountImages, countTotalAmount(
                recyclerAdapter.artPhoto))
            madeDelete = true

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
        Glide.with(requireContext())
            .load("https://www.google.es/images/srpr/logo11w.png") // your URL
            //.bi()
            .into(object : SimpleTarget<Bitmap?>(100, 100) {
                fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation?) {
                    val f = File(context!!.cacheDir, filename) // use your filename fully
                    f.createNewFile()
                    //Convert bitmap to byte array
                    val bos = ByteArrayOutputStream()
                    resource.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                    val bitmapdata: ByteArray = bos.toByteArray()
                    //write the bytes in file
                    val fos = FileOutputStream(f)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                    val U = Uri.fromFile(f)
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "image/png"
                    i.putExtra(Intent.EXTRA_STREAM, U)
                    startActivity(Intent.createChooser(i, "Email:"))
                }
            })

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:" + "yve001@uit.no")
                putExtra(Intent.EXTRA_SUBJECT, "Ordreinformasjon")
                putExtra(Intent.EXTRA_TEXT, generateMessageBody())
            }
        startActivity(intent)
        reset()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun reset() {
        viewModel.resetCart()
        recyclerAdapter.artPhoto = mutableListOf()
        recyclerAdapter.notifyDataSetChanged()
        binding.TotalPrice.text = getString(R.string.total_price, 0)
        binding.AmountImages.text = getString(R.string.amountImages, 0)
        Toast.makeText(requireContext(), "Takk for din bestilling!", Toast.LENGTH_SHORT).show()
    }


    private fun generateMessageBody(): String {
        var imagesIndex = "\nBildeindekser:\n"
        var message = "Kunstner:\n"

        viewModel.cart.value!!.forEachIndexed { index, item ->
            imagesIndex += item.photo.id.toString()
            message += item.cartPhotoDB.artistName
            if (index < viewModel.cart.value!!.size - 1) {
                imagesIndex += ", "
                message += ", "
            }
            else {
                imagesIndex += ". "
                message += ". "
            }
        }
        return message + imagesIndex + "\nTotalpris:\n" + countTotalPrice(recyclerAdapter.artPhoto)
            .toString() + "."
    }

    private fun setRecyclerData() {
        requestData()
        viewModel.cart.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerAdapter.artPhoto = it
                binding.TotalPrice.text = getString(R.string.total_price, countTotalPrice(it))
                binding.AmountImages.text = getString(R.string.amountImages, countTotalAmount(it))
            }
        }
    }

    private fun requestData() {
        viewModel.getAllCartPhotos()
    }

    private fun countTotalAmount(cartPhotos: MutableList<CartPhoto>): Int {
        return cartPhotos.sumOf { it.cartPhotoDB.amount }
    }

    private fun countTotalPrice(cartPhotos: MutableList<CartPhoto>): Int {
        var price = 0
        cartPhotos.forEach {
            price += it.cartPhotoDB.price * it.cartPhotoDB.amount
        }
        return price
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getAllCartPhotos()
        madeDelete = false
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

//    private fun deleteFromCart(cartPhotoDB: CartPhotoDB) {
//        viewModel.deleteByItem(cartPhotoDB)
//    }

}


