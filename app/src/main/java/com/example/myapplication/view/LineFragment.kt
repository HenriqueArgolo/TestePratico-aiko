package com.example.myapplication.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.LineAdapter
import com.example.myapplication.adapter.ListItem
import com.example.myapplication.databinding.FragmentLineBinding
import com.example.myapplication.viewModel.LineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LineFragment : Fragment() {
    private lateinit var binding: FragmentLineBinding
    private lateinit var lineAdapter: LineAdapter

    companion object {
        fun newInstance() = LineFragment()
    }

    private val viewModel: LineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLineBinding.inflate(inflater, container, false)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineAdapter = LineAdapter(requireContext(), mutableListOf())
        configRv()
        viewModel.lineList.observe(viewLifecycleOwner, Observer { lines ->
            val linesMutable = lines.map { ListItem.Line(it) }
            lineAdapter.setData(linesMutable)
        })

        searchLine()
    }

    private fun configRv(){
        binding.rv.apply {
            adapter = lineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun searchLine(){
        binding.searchBtn.setOnClickListener {
            val termoBusca = binding.searchInput.text.toString()
            CoroutineScope(Dispatchers.IO).apply {
                viewModel.fetchBusLine(termoBusca)
            }
            configRv()
            binding.informationContainer.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE
        }
    }
}