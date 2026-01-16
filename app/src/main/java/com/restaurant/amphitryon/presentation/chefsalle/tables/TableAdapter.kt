package com.restaurant.amphitryon.presentation.chefsalle.tables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.databinding.ItemTableBinding

class TableAdapter : ListAdapter<TableEntity, TableAdapter.TableViewHolder>(TableDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = ItemTableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TableViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class TableViewHolder(
        private val binding: ItemTableBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(table: TableEntity) {
            binding.tvTableNumero.text = "Table ${table.numero}"
            binding.tvTableSeats.text = "${table.nombrePlaces} places"
            binding.tvTableStatus.text = if (table.estOccupee) "Occupée" else "Disponible"
        }
    }
    
    private class TableDiffCallback : DiffUtil.ItemCallback<TableEntity>() {
        override fun areItemsTheSame(oldItem: TableEntity, newItem: TableEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: TableEntity, newItem: TableEntity): Boolean {
            return oldItem == newItem
        }
    }
}
