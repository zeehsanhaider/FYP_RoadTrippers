package com.example.roadtrippers.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roadtrippers.R
import com.example.roadtrippers.databinding.LayoutItemSelectVehicleBinding
import com.example.roadtrippers.model.Vehicle

class VehiclesAdapter(
    private val context: Context
) : RecyclerView.Adapter<VehiclesAdapter.VehicleItemViewHolder>() {

    var selectedPosition = -1

    /**
     * VIEW HOLDER
     */
    inner class VehicleItemViewHolder(val binding: LayoutItemSelectVehicleBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleItemViewHolder {
        return VehicleItemViewHolder(
            LayoutItemSelectVehicleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VehicleItemViewHolder, position: Int) {
        val vehicle = differ.currentList[position]
        with(holder.binding) {
            txName.text = vehicle.name
            txExpenseFactorValue.text = "${vehicle.expenseFactor} %"
            txMileage.text = "${vehicle.mileage} Km"
            txSeats.text = "${vehicle.seats} Seats"
            // Loading product image from url
            Glide.with(context).load(vehicle.imgUrl).into(ivVehicleImg)
            // Item-view on click listener
            parentCard.setOnClickListener {
                onItemClickListener?.let { it(vehicle, position) }
            }
            // Giving colors to selected and un-selected items
            if (selectedPosition == position) {
                parentCard.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.grey_10)
                )
            } else {
                parentCard.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.grey_3)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * DIFF CALLBACK
     */
    private val differCallback = object : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((vehicle: Vehicle, position: Int) -> Unit)? =
        null

    fun setOnItemClickListener(listener: (vehicle: Vehicle, position: Int) -> Unit) {
        onItemClickListener = listener
    }

    fun getSelectedVehicle(): Vehicle {
        return differ.currentList[selectedPosition]
    }

}