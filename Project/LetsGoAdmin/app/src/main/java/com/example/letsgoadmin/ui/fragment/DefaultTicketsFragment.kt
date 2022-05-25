package com.example.letsgoadmin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.letsgoadmin.databinding.FragmentDefaultTicketsBinding

class DefaultTicketsFragment : Fragment() {

    private lateinit var binding: FragmentDefaultTicketsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDefaultTicketsBinding.inflate(inflater, container, false)
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
        private const val TAG = "DefaultTicketsFragment"
    }
}