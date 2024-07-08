package com.example.myapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.BusLine

class LineViewModel : ViewModel() {
    private val _listOfLine = MutableLiveData<MutableList<BusLine>>()
}