package com.example.myapplication.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import com.example.myapplication.R
import com.example.myapplication.adapter.LineAdapter
import com.example.myapplication.databinding.FragmentLineBinding
import com.example.myapplication.viewModel.LineViewModel

class LineFragment : Fragment() {
    private lateinit var binding: FragmentLineBinding
    private lateinit var adapter: LineAdapter

    companion object {
        fun newInstance() = LineFragment()
    }

    private val viewModel: LineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLineBinding.inflate(inflater, container, false)
        return  binding.root
    }

    private fun configRv(){
        adapter = LineAdapter(requireContext())
    }
}