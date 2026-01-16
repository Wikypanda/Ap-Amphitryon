package com.restaurant.amphitryon.presentation.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.restaurant.amphitryon.databinding.ActivityMainBinding
import com.restaurant.amphitryon.presentation.chefcuisinier.ChefCuisinierActivity
import com.restaurant.amphitryon.presentation.chefsalle.ChefSalleActivity
import com.restaurant.amphitryon.presentation.serveur.ServeurActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnChefSalle.setOnClickListener {
            startActivity(Intent(this, ChefSalleActivity::class.java))
        }
        
        binding.btnChefCuisinier.setOnClickListener {
            startActivity(Intent(this, ChefCuisinierActivity::class.java))
        }
        
        binding.btnServeur.setOnClickListener {
            startActivity(Intent(this, ServeurActivity::class.java))
        }
    }
}
