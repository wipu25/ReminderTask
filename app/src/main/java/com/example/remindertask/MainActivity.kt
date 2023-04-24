package com.example.remindertask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.remindertask.databinding.ActivityMainBinding
import com.example.remindertask.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        if(mainActivityViewModel.getIsIntroSeen()) {
            val navController = (supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment).navController
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(R.id.main)
            navController.graph = graph
        }
        setContentView(binding.root)
    }
}