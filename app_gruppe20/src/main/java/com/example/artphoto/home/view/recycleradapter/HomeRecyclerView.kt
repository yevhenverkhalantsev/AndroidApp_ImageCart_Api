package com.example.artphoto.home.view.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.images.model.CartPhoto

class HomeRecyclerView : RecyclerView.Adapter<HomeViewHolder>() {
    var artPhoto : List<CartPhoto> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

    }

    override fun getItemCount(): Int = artPhoto.size
}