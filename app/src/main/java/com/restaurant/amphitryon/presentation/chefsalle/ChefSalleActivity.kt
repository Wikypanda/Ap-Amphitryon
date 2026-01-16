package com.restaurant.amphitryon.presentation.chefsalle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.restaurant.amphitryon.databinding.ActivityChefSalleBinding
import com.restaurant.amphitryon.presentation.chefsalle.tables.TableAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChefSalleActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityChefSalleBinding
    private val viewModel: ChefSalleViewModel by viewModels()
    private lateinit var tableAdapter: TableAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefSalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        observeTables()
        setupClickListeners()
    }
    
    private fun setupRecyclerView() {
        tableAdapter = TableAdapter()
        binding.recyclerViewTables.apply {
            layoutManager = LinearLayoutManager(this@ChefSalleActivity)
            adapter = tableAdapter
        }
    }
    
    private fun observeTables() {
        lifecycleScope.launch {
            viewModel.tables.collect { tables ->
                tableAdapter.submitList(tables)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddTable.setOnClickListener {
            // TODO: Show dialog to add new table
        }
    }
}
