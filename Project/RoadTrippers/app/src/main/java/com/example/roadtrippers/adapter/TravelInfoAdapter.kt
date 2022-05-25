package com.example.roadtrippers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtrippers.databinding.LayoutItemInfoBinding

class TravelInfoAdapter(
    private val info: ArrayList<String>
) : RecyclerView.Adapter<TravelInfoAdapter.TravelInfoItemViewHolder>() {

    /**
     * VIEW HOLDER
     */
    inner class TravelInfoItemViewHolder(val binding: LayoutItemInfoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelInfoItemViewHolder {
        return TravelInfoItemViewHolder(
            LayoutItemInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TravelInfoItemViewHolder, position: Int) {
        holder.binding.txInfo.text = info[position]
    }

    override fun getItemCount(): Int {
        return info.size
    }


}