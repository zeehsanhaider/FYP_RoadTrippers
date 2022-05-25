package com.example.roadtrippers.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roadtrippers.databinding.LayoutItemBookedTripBinding
import com.example.roadtrippers.model.DefaultTripTicket
import com.example.roadtrippers.model.Trip

class MyTripsAdapter(
    private val context: Context,
    private val trips: ArrayList<Trip>,
    private val tripTickets: ArrayList<DefaultTripTicket>
) : RecyclerView.Adapter<MyTripsAdapter.MyTripItemViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class MyTripItemViewHolder(val binding: LayoutItemBookedTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * ADAPTER METHODS
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTripItemViewHolder {
        return MyTripItemViewHolder(
            LayoutItemBookedTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyTripItemViewHolder, position: Int) {
        val trip = trips[position]
        val tripTicket = tripTickets[position]

        with(holder.binding) {
            // From trip
            txTripTitle.text = trip.title
            txPerPersonFair.text = "${trip.expensePerPerson} Rs"
            // From trip ticket
            txTotalExpense.text = "${tripTicket.totalExpense} Rs"
            txPersons.text = "${tripTicket.persons}"
            txContact.text = tripTicket.contact
            // Loading trip image
            Glide.with(context).load(trip.imageUrls.split(',')[0]).into(ivTripImg)
            // Item Click Listener
            cardParent.setOnClickListener {
                onItemClickListener?.let { it(trip, tripTicket, position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    /**
     * ITEM CLICK LISTENERS
     */
    private var onItemClickListener: ((tripItem: Trip, tripTicket: DefaultTripTicket, position: Int) -> Unit)? =
        null

    fun setOnItemClickListener(listener: (tripItem: Trip, tripTicket: DefaultTripTicket, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}