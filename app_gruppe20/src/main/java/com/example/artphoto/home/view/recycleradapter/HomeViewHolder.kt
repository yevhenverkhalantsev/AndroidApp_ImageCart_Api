package com.example.artphoto.home.view.recycleradapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.artphoto.R

class HomeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image: ImageView = itemView.findViewById(R.id.image)
    val title: TextView = itemView.findViewById(R.id.title)
    val size: TextView = itemView.findViewById(R.id.size)
    val frame: TextView = itemView.findViewById(R.id.frame)
    val price: TextView = itemView.findViewById(R.id.priceAndAmount)

}
