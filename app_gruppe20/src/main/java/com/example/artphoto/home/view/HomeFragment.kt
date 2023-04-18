package com.example.artphoto.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.artphoto.R
import com.example.artphoto.context.MyApplication
import com.example.artphoto.databinding.FragmentHomeBinding
import com.example.artphoto.home.view.recycleradapter.HomeRecyclerView
import com.example.artphoto.images.model.CartPhoto
import com.example.artphoto.images.viewmodel.ImagesViewModel
import javax.inject.Inject


class HomeFragment : Fragment() {
    @Inject lateinit var viewModel: ImagesViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerAdapter: HomeRecyclerView
    private var madeDelete: Boolean = false

    override fun onAttach(context: Context) {
        (requireContext().applicationContext as MyApplication).appComponent.inject(this)
        super.onAttach(context)
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
            viewModel.clearPhotos()
            if (!cartIsEmpty()) sendBuyingMessage()
            else {
                Toast.makeText(requireContext(), getString(R.string.choose_picture), Toast.LENGTH_SHORT).show()
            }
        }

        binding.resetButton.setOnClickListener {

            if (recyclerAdapter.artPhoto.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.cart_is_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.clearPhotos()
            recyclerAdapter.artPhoto = mutableListOf()
            binding.TotalPrice.text = getString(R.string.total_price, 0)
            binding.AmountImages.text = getString(R.string.amountImages, 0)
        }

        binding.deleteButton.setOnClickListener {

            if (recyclerAdapter.artPhoto.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.cart_is_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (recyclerAdapter.selectedCartPhotos.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.choose_item_to_delete), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tempCartPhotos = recyclerAdapter.selectedCartPhotos.toMutableList()
            viewModel.deleteSelectedPhotos(tempCartPhotos)
            recyclerAdapter.deleteSelectedItems()
            binding.TotalPrice.text = getString(R.string.total_price, countTotalPrice(
                recyclerAdapter.artPhoto))
            binding.AmountImages.text = getString(R.string.amountImages, countTotalAmount(
                recyclerAdapter.artPhoto))
            madeDelete = true

        }

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
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("yve001@uit.no"))
            putExtra(Intent.EXTRA_SUBJECT, "Ordreinformasjon")
            putExtra(Intent.EXTRA_TEXT, generateMessageBody())
        }
        startActivity(Intent.createChooser(intent, "Send epost"))
        reset()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun reset() {
        viewModel.resetCart()
        recyclerAdapter.artPhoto = mutableListOf()
        recyclerAdapter.notifyDataSetChanged()
        binding.TotalPrice.text = getString(R.string.total_price, 0)
        binding.AmountImages.text = getString(R.string.amountImages, 0)
        Toast.makeText(requireContext(), getString(R.string.thanks_for_order), Toast.LENGTH_SHORT).show()
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
        return "$message$imagesIndex\nTotalpris:\n" + countTotalPrice(recyclerAdapter.artPhoto)
            .toString() + "."
    }

    private fun setRecyclerData() {
        viewModel.cart.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerAdapter.artPhoto = it
                binding.TotalPrice.text = getString(R.string.total_price, countTotalPrice(it))
                binding.AmountImages.text = getString(R.string.amountImages, countTotalAmount(it))
            }
        }
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

}