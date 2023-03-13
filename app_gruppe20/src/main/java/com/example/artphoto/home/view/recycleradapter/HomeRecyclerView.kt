package com.example.artphoto.home.view.recycleradapter

import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.images.model.CartPhoto

class HomeRecyclerView(private val onClickImage: View.OnClickListener) : RecyclerView.Adapter<HomeViewHolder>() {
    private var artPhoto : List<CartPhoto> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        Log.i("test", "OnCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        Log.i("test", "OnBindViewHolder")

        val repository = artPhoto[position]
        holder.title.text = repository.photo.title
        Glide.with(holder.itemView.context).load(repository.photo.thumbnailUrl).into(holder.image)
        holder.price.text = repository.price.toString()
        holder.frame.text = repository.frame.name
        holder.size.text = repository.size.name

    }

    override fun getItemCount(): Int = artPhoto.size
}