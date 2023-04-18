package com.example.artphoto.home.view.recycleradapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.images.model.CartPhoto

class HomeRecyclerView : RecyclerView.Adapter<HomeViewHolder>() {
    var artPhoto : MutableList<CartPhoto> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val selectedItems = mutableListOf<Int>()
    private val _selectedCartPhotos = mutableListOf<CartPhoto>()
    val selectedCartPhotos get() = _selectedCartPhotos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.raw_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val cardPhoto = artPhoto[position]
        Glide.with(holder.itemView.context).load(cardPhoto.photo.thumbnailUrl).into(holder.image)
        holder.title.text = cardPhoto.photo.title
        holder.size.text = holder.itemView.context.getString(R.string.title_size, cardPhoto.size.name)
        holder.frame.text = holder.itemView.context.getString(R.string.title_frame, cardPhoto.frame.name)
        holder.price.text = holder.itemView.context.getString(R.string.price_amount_total_price,
            cardPhoto.cartPhotoDB.price, cardPhoto.cartPhotoDB.amount, cardPhoto.cartPhotoDB.price * cardPhoto.cartPhotoDB .amount)
        holder.checkBox.isVisible = false

        holder.itemView.setOnLongClickListener {
            selectItem(holder,cardPhoto, position)
        }

        holder.itemView.setOnClickListener {
            if (isItemSelected(position)) unselectItem(holder, cardPhoto, position)
        }
    }

    private fun unselectItem(holder: HomeViewHolder, cartPhoto: CartPhoto, position: Int) {
        holder.checkBox.isVisible = false
        selectedItems.remove(position)
        _selectedCartPhotos.remove(cartPhoto)
    }

    private fun isItemSelected(position: Int): Boolean {
        return selectedItems.contains(position)
    }

    private fun selectItem(holder: HomeViewHolder, cartPhoto: CartPhoto, position: Int): Boolean {
        return if (isItemSelected(position)) {
            unselectItem(holder,cartPhoto,  position)
            true
        } else {
            holder.checkBox.isVisible = true
            selectedItems.add(position)
            _selectedCartPhotos.add(cartPhoto)
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteSelectedItems() {
        if (selectedItems.isEmpty()) return
        selectedCartPhotos.forEachIndexed { _, cartPhoto ->
            artPhoto.remove(cartPhoto)
        }
        if (selectedItems.size == 1) notifyItemRemoved(selectedItems[0])
        else notifyDataSetChanged()
        selectedItems.clear()
        _selectedCartPhotos.clear()
    }

    override fun getItemCount(): Int = artPhoto.size
}