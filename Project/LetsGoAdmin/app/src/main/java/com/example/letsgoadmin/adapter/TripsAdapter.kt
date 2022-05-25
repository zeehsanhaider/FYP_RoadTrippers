package com.example.letsgoadmin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsgoadmin.databinding.LayoutItemTripBinding
import com.example.letsgoadmin.model.Trip

class TripsAdapter(private val context: Context) :
    RecyclerView.Adapter<TripsAdapter.TripItemViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class TripItemViewHolder(val binding: LayoutItemTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripItemViewHolder {
        return TripItemViewHolder(
            LayoutItemTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TripItemViewHolder, position: Int) {
        val tripItem = differ.currentList[position]
        with(holder.binding) {
            // Product title
            txTripTitle.text = tripItem.title
            // Product description
            txTripDescription.text = tripItem.description
            // Product price
            txPerPersonFair.text = "${tripItem.expensePerPerson} Rs"
            // Loading product image from url
            Glide.with(context).load(tripItem.imageUrls.split(',')[0]).into(ivTripImg)
            // Item-view on click listener
            cardParent.setOnClickListener {
                onItemClickListener?.let { it(tripItem, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * DIFF CALLBACK
     */
    private val differCallback = object : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((tripItem: Trip, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (tripItem: Trip, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}