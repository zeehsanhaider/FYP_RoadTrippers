package com.example.letsgoadmin.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.letsgoadmin.databinding.FragmentCustomTripDetailsBinding
import com.example.letsgoadmin.util.animation.Tools
import com.example.letsgoadmin.util.animation.ViewAnimation
import com.example.letsgoadmin.util.data.DataGenerator

class CustomTripDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCustomTripDetailsBinding
    private val args: CustomTripDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomTripDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        Log.e(TAG, "initComponents: place ${args.place}")
        Log.e(TAG, "initComponents: ticket ${args.ticket}")
        Log.e(TAG, "initComponents: user ${args.user}")

        setData()
        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        with(binding) {
            txCustomerName.text = args.user.name
            Glide.with(requireContext()).load(args.place.imageUrls.split(',')[0]).into(ivTripImg)
            txDepartureDateTime.text = "${args.ticket.departureDate} - ${args.ticket.departureTime}"
            txDays.text = "${args.ticket.days} days"
            txPerPersonFair.text = "${args.place.expensePerPerson} Rs"
            txPersons.text = "${args.ticket.persons}"
            txTotalExpense.text = "${args.ticket.totalExpense} Rs"
            txTotalExpenseMini.text = "${args.ticket.totalExpense} Rs"
            txTripName.text = args.place.title
            txTripDescription.text = args.place.description
            txVehicle.text = DataGenerator.getVehicleNameById(args.ticket.vehicleId)
        }
    }

    private fun setListeners() {
        binding.btnToggleTicketDetails.setOnClickListener {
            toggleSection(it, binding.lytExpandTripDetails)
        }

        binding.btnTogglePlaceDesc.setOnClickListener {
            toggleSection(it, binding.lytExpandDescription)
        }
    }

    private fun toggleSection(bt: View, lyt: View) {
        val show = toggleArrow(bt)
        if (show) {
            ViewAnimation.expand(lyt, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(binding.nestedScrollView, lyt)
                }
            })
        } else {
            ViewAnimation.collapse(lyt)
        }
    }

    private fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    companion object {
        private const val TAG = "CustomTripDetailsFrag"
    }

}