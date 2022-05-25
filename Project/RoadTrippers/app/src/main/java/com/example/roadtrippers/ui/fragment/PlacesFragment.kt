package com.example.roadtrippers.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roadtrippers.adapter.PlacesAdapter
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentPlacesBinding
import com.example.roadtrippers.model.Place
import com.example.roadtrippers.util.extension.setLinearLayoutManager
import com.example.roadtrippers.util.toast.ToastUtil
import org.koin.android.ext.android.inject

class PlacesFragment : Fragment() {

    private lateinit var binding: FragmentPlacesBinding
    private lateinit var placesAdapter: PlacesAdapter
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {

        setLinearLayoutManager(
            requireContext(),
            binding.rvPlaces,
            true
        )

        placesAdapter = PlacesAdapter(requireContext())
        binding.rvPlaces.adapter = placesAdapter


        databaseController.getAllPlaces(object : DataChangeListener {
            override fun onDataChange(list: List<Any>) {
                val incomingPlaces: List<Place> = list.filterIsInstance<Place>()
                placesAdapter.differ.submitList(incomingPlaces)
            }

            override fun onCancel(message: String) {
                toastUtil.shortToast(message)
            }
        })

        setListeners()
    }

    private fun setListeners() {
        placesAdapter.setOnItemClickListener { place, _ ->
            findNavController().navigate(
                PlacesFragmentDirections.actionPlacesFragmentToPlaceDetailsFragment(
                    selectedPlace = place
                )
            )
        }
    }

    companion object {
        private const val TAG = "PlacesFragment"
    }

}