package com.example.newslive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newslive.R
import com.example.newslive.models.Article
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private lateinit var mylistner : onItemClickListener

    interface onItemClickListener{
        fun onItemClicking(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        mylistner = listener
    }


    inner class MyViewHolder(itemView : View, listener : onItemClickListener) : RecyclerView.ViewHolder(itemView){
        private val title : TextView
        private val imageview : ShapeableImageView
        init {
            title = itemView.findViewById(R.id.headingTitle)
            imageview = itemView.findViewById(R.id.headingImage)
            itemView.setOnClickListener {
                listener.onItemClicking(adapterPosition)
            }
        }

    }

    private val differcallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_row,parent,false)
        return MyViewHolder(itemView,mylistner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = differ.currentList[position] // Change DataArray to articles

        holder.itemView.apply {
            val tvTitle: TextView = findViewById(R.id.headingTitle)
            val image: ShapeableImageView = findViewById(R.id.headingImage)

            tvTitle.text = currentItem.title
            if ( currentItem.urlToImage != null ){
                Picasso.get().load(currentItem.urlToImage).into(image)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}


