package com.example.roadtrippers.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.roadtrippers.adapter.TravelInfoAdapter
import com.example.roadtrippers.databinding.FragmentTravelInfoBinding
import com.example.roadtrippers.util.extension.setLinearLayoutManager

class TravelInfoFragment : Fragment() {

    private lateinit var binding: FragmentTravelInfoBinding
    private lateinit var precautionsAdapter: TravelInfoAdapter
    private lateinit var thingsToBringAdapter: TravelInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTravelInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        setLinearLayoutManager(
            requireContext(),
            binding.rvPrecautions,
            true
        )
        setLinearLayoutManager(
            requireContext(),
            binding.rvThingsToBring,
            true
        )
        precautionsAdapter = TravelInfoAdapter(getTravelPrecautionsList())
        thingsToBringAdapter = TravelInfoAdapter(getThingsToBringList())

        binding.rvPrecautions.adapter = precautionsAdapter
        binding.rvThingsToBring.adapter = thingsToBringAdapter
    }

    private fun getTravelPrecautionsList(): ArrayList<String> {
        val precautions = ArrayList<String>()
        precautions.add("Dues must be paid before registration.")
        precautions.add("Participants must hold a computerized CNIC/Passport Card.")
        precautions.add("Participants are advised to use non-slippery shoes/boots/Joggers/DMS.")
        precautions.add("Participants must not wear heels/dress shoes.")
        precautions.add("Nature Explorer Tours will not be responsible for any injury/damage/loss.")
        return precautions
    }

    private fun getThingsToBringList(): ArrayList<String> {
        val thingsToBringList = ArrayList<String>()
        thingsToBringList.add("Snacks")
        thingsToBringList.add("Umbrella")
        thingsToBringList.add("Joggers")
        thingsToBringList.add("Cap")
        thingsToBringList.add("Torch")
        thingsToBringList.add("Rain coat")
        thingsToBringList.add("Power Bank")
        thingsToBringList.add("Jackets, Upper / Shall")
        thingsToBringList.add("Warm Clothes or Extra Clothes")
        thingsToBringList.add("Face Mask")
        thingsToBringList.add("Hand Sanitizer")
        thingsToBringList.add("Sunglasses")
        thingsToBringList.add("Water bottle(must)")
        thingsToBringList.add("Edibles")
        thingsToBringList.add("Medication if any")
        return thingsToBringList
    }

    companion object {
        private const val TAG = "TravelInfoFragment"
    }
}