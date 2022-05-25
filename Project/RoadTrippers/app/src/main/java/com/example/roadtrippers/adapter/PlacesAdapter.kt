package com.example.roadtrippers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roadtrippers.databinding.LayoutItemTripBinding
import com.example.roadtrippers.model.Place
import com.example.roadtrippers.util.extension.gone

class PlacesAdapter(private val context: Context) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class PlaceViewHolder(val binding: LayoutItemTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            LayoutItemTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = differ.currentList[position]
        with(holder.binding) {
            // Product title
            txTripTitle.text = place.title
            // Product description
            txTripDescription.text = place.description
            // Making some text-views gone
            txTripDays.gone()
            dash.gone()
            // Product price
            txPerPersonFair.text = "${place.expensePerPerson} Rs"
            // Loading product image from url
            Glide.with(context).load(place.imageUrls.split(',')[0]).into(ivTripImg)
            // Item-view on click listener
            cardParent.setOnClickListener {
                onItemClickListener?.let { it(place, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * DIFF CALLBACK
     */
    private val differCallback = object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((place: Place, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (place: Place, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}