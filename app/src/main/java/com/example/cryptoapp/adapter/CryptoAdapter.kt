package com.example.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.model.CryptoData
import kotlin.random.Random

class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cryptoImage: ImageView = itemView.findViewById(R.id.imgViewCrypto)
        val cryptoPattern: ImageView = itemView.findViewById(R.id.imageViewPattern)
        val cryptoName: TextView = itemView.findViewById(R.id.tvCryptoName)
        val cryptoPrice: TextView = itemView.findViewById(R.id.tvCryptoPrice)
    }

    private val differCallBack = object : DiffUtil.ItemCallback<CryptoData>() {
        override fun areItemsTheSame(oldItem: CryptoData, newItem: CryptoData): Boolean {
            return oldItem.name_full == newItem.name_full
        }

        override fun areContentsTheSame(oldItem: CryptoData, newItem: CryptoData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_home_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val cryptoData = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(cryptoData.icon_url).into(holder.cryptoImage)
            holder.cryptoName.text = cryptoData.name_full
            holder.cryptoPrice.text = "$" + cryptoData.rate.toString()
            setRandomImage(holder.cryptoPattern)
        }
    }


    override fun getItemCount(): Int = differ.currentList.size

    // Generate a random index within the range of the imageArray
    private fun setRandomImage(imageView: ImageView) {
        val randomIndex = Random.nextInt(imageArray.size)

        // Get the resource ID corresponding to the random index
        val randomImageResourceId = imageArray[randomIndex]

        // Set the randomly chosen image to the ImageView
        imageView.setImageResource(randomImageResourceId)
    }

    private val imageArray = arrayOf(
        R.drawable.pattern1,
        R.drawable.pattern2,
        R.drawable.pattern3,
        R.drawable.pattern4
    )

}