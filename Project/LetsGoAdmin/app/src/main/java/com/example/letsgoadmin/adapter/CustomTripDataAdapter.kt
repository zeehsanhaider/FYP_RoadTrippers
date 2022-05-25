package com.example.letsgoadmin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsgoadmin.databinding.LayoutItemDefaultTripBinding
import com.example.letsgoadmin.databinding.LayoutItemDefaultTripDataBinding
import com.example.letsgoadmin.model.CustomTripData

class CustomTripDataAdapter(
    private val context: Context,
    private val customTripsData: List<CustomTripData>
) : RecyclerView.Adapter<CustomTripDataAdapter.CustomTripDataViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class CustomTripDataViewHolder(val binding: LayoutItemDefaultTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomTripDataViewHolder {
        return CustomTripDataViewHolder(
            LayoutItemDefaultTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomTripDataViewHolder, position: Int) {
        val item = customTripsData[position]
        with(holder.binding) {
            txTripName.text = item.place.title
            txTotalExpense.text = "${item.ticket.totalExpense} Rs"
            Glide.with(context).load(item.place.imageUrls.split(',')[0]).into(ivTripImg)

            txPersons.text = "${item.ticket.persons} Persons"
            txDays.text = "${item.ticket.days} Days"
            txContact.text = item.ticket.contact

            cardParent.setOnClickListener {
                onItemClickListener?.let { it(item, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return customTripsData.size
    }

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((clickedItem: CustomTripData, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (clickedItem: CustomTripData, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}