package com.example.artphoto.images.view.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artphoto.R
import com.example.artphoto.images.model.ArtPhoto

class ImageRecyclerView(private val onItemClicked: (ArtPhoto) -> Unit): ListAdapter<ArtPhoto, ImageRecyclerView.ArtPhotoViewHolder>(DiffCallback) {
    inner class ArtPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.raw_art_photo)
        fun bind(artPhoto: ArtPhoto) {
            Glide.with(itemView.context)
                .load(artPhoto.thumbnailUrl)
                .into(image)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageRecyclerView.ArtPhotoViewHolder {
        return ArtPhotoViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.raw_art_photo, parent, false))
    }
    override fun onBindViewHolder(holder: ImageRecyclerView.ArtPhotoViewHolder, position: Int) {
        val artPhoto = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(artPhoto)
        }
        holder.bind(artPhoto) }
    companion object DiffCallback : DiffUtil.ItemCallback<ArtPhoto>() {
        override fun areItemsTheSame(oldItem: ArtPhoto, newItem: ArtPhoto): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ArtPhoto, newItem: ArtPhoto): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }
    }
}