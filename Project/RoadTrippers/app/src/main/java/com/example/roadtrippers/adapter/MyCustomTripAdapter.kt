package com.example.roadtrippers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roadtrippers.databinding.LayoutItemBookedTripBinding
import com.example.roadtrippers.model.CustomTripTicket
import com.example.roadtrippers.model.Place

class MyCustomTripAdapter(
    private val context: Context,
    private val places: ArrayList<Place>,
    private val customTripTickets: ArrayList<CustomTripTicket>
) : RecyclerView.Adapter<MyCustomTripAdapter.MyCustomTripItemViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class MyCustomTripItemViewHolder(val binding: LayoutItemBookedTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCustomTripItemViewHolder {
        return MyCustomTripItemViewHolder(
            LayoutItemBookedTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyCustomTripItemViewHolder, position: Int) {
        val place = places[position]
        val customTripTicket = customTripTickets[position]

        with(holder.binding) {
            // From trip
            txTripTitle.text = place.title
            txPerPersonFair.text = "${place.expensePerPerson} Rs"
            // From trip ticket
            txTotalExpense.text = "${customTripTicket.totalExpense} Rs"
            txPersons.text = "${customTripTicket.persons}"
            txContact.text = customTripTicket.contact
            // Loading trip image
            Glide.with(context).load(place.imageUrls.split(',')[0]).into(ivTripImg)
            // Item Click Listener
            cardParent.setOnClickListener {
                onItemClickListener?.let { it(place, customTripTicket, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return customTripTickets.size
    }

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((place: Place, customTripTicket: CustomTripTicket, position: Int) -> Unit)? =
        null

    fun setOnItemClickListener(listener: (place: Place, customTripTicket: CustomTripTicket, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}