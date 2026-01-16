package com.restaurant.amphitryon.presentation.chefcuisinier

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.restaurant.amphitryon.databinding.ActivityChefCuisinierBinding
import com.restaurant.amphitryon.presentation.chefcuisinier.plats.PlatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChefCuisinierActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityChefCuisinierBinding
    private val viewModel: ChefCuisinierViewModel by viewModels()
    private lateinit var platAdapter: PlatAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefCuisinierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        observePlats()
        setupClickListeners()
    }
    
    private fun setupRecyclerView() {
        platAdapter = PlatAdapter()
        binding.recyclerViewPlats.apply {
            layoutManager = LinearLayoutManager(this@ChefCuisinierActivity)
            adapter = platAdapter
        }
    }
    
    private fun observePlats() {
        lifecycleScope.launch {
            viewModel.plats.collect { plats ->
                platAdapter.submitList(plats)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddPlat.setOnClickListener {
            // TODO: Show dialog to add new plat
        }
    }
}
