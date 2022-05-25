package com.example.letsgoadmin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsgoadmin.R
import com.example.letsgoadmin.databinding.FragmentCustomTicketsBinding


class CustomTicketsFragment : Fragment() {

    private lateinit var binding: FragmentCustomTicketsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        setListeners()
    }

    private fun setListeners() {

    }

    companion object {
        private const val TAG = "CustomTicketsFragment"
    }
}