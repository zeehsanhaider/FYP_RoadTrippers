package com.example.letsgoadmin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsgoadmin.databinding.LayoutItemDefaultTripBinding
import com.example.letsgoadmin.model.DefaultTripData

class DefaultTripDataAdapter(
    private val context: Context,
    private val defaultTripsData: ArrayList<DefaultTripData>
) : RecyclerView.Adapter<DefaultTripDataAdapter.DefaultTripDataViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class DefaultTripDataViewHolder(val binding: LayoutItemDefaultTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultTripDataViewHolder {
        return DefaultTripDataViewHolder(
            LayoutItemDefaultTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DefaultTripDataViewHolder, position: Int) {
        val item = defaultTripsData[position]
        with(holder.binding) {
            txTripName.text = item.trip.title
            txTotalExpense.text = "${item.ticket.totalExpense} Rs"
            Glide.with(context).load(item.trip.imageUrls.split(',')[0]).into(ivTripImg)

            txPersons.text = "${item.ticket.persons} Persons"
            txDays.text = "${item.trip.tripDays} Days"
            txContact.text = item.ticket.contact

            cardParent.setOnClickListener {
                onItemClickListener?.let { it(item, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return defaultTripsData.size
    }

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((clickedItem: DefaultTripData, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (clickedItem: DefaultTripData, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}