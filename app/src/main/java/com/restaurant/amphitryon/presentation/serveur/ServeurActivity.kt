package com.restaurant.amphitryon.presentation.serveur

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.restaurant.amphitryon.databinding.ActivityServeurBinding
import com.restaurant.amphitryon.presentation.serveur.commandes.CommandeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServeurActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityServeurBinding
    private val viewModel: ServeurViewModel by viewModels()
    private lateinit var commandeAdapter: CommandeAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServeurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        observeCommandes()
        setupClickListeners()
    }
    
    private fun setupRecyclerView() {
        commandeAdapter = CommandeAdapter()
        binding.recyclerViewCommandes.apply {
            layoutManager = LinearLayoutManager(this@ServeurActivity)
            adapter = commandeAdapter
        }
    }
    
    private fun observeCommandes() {
        lifecycleScope.launch {
            viewModel.commandes.collect { commandes ->
                commandeAdapter.submitList(commandes)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddCommande.setOnClickListener {
            // TODO: Show dialog to add new commande
        }
    }
}
