package com.example.roadtrippers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roadtrippers.databinding.LayoutItemTripPicBinding

class TripPicsAdapter(private val context: Context, private val tripPics: List<String>) :
    RecyclerView.Adapter<TripPicsAdapter.TripPicViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class TripPicViewHolder(val binding: LayoutItemTripPicBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripPicViewHolder {
        return TripPicViewHolder(
            LayoutItemTripPicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TripPicViewHolder, position: Int) {
        val currPicUrl = tripPics[position]
        with(holder.binding) {
            // Loading product image from url
            Glide.with(context).load(currPicUrl).into(ivTripImg)
            // Item click listener
            cardParent.setOnClickListener {
                onItemClickListener?.let { it(currPicUrl, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return tripPics.size
    }

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((imgUrl: String, position: Int) -> Unit)? =
        null

    fun setOnItemClickListener(listener: (imgUrl: String, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}