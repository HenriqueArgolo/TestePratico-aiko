package com.example.myapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.infra.Retrofit
import com.example.myapplication.infra.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mapFragment = MapFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            Retrofit().apiService.auth(Token.value.TOKEN)
        }

        changeFragment(mapFragment)
        navOptions()


    }


    private fun navOptions() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.lines -> {
                    val line = LineFragment()
                    changeFragment(line)
                    true
                }

                R.id.busStop -> {
                    changeFragment(mapFragment)
                    true
                }
                R.id.bus -> {
                    val bus = BusFragment()
                    true
                }

                else -> false
            }
        }
    }


    private fun changeFragment(fragment: Fragment) {
        val navigation = supportFragmentManager.beginTransaction()
        navigation.replace(R.id.mainContainer, fragment)
        navigation.commit()
    }



}