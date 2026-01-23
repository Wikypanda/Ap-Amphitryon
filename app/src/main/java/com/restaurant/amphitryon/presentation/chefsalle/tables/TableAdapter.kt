package com.restaurant.amphitryon.presentation.chefsalle.tables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.databinding.ItemTableBinding

class TableAdapter(
    private val onTableClick: (TableEntity) -> Unit = {},
    private val onTableLongClick: (TableEntity) -> Unit = {},
    private val onAffectClick: (TableEntity) -> Unit = {}
) : ListAdapter<TableEntity, TableAdapter.TableViewHolder>(TableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = ItemTableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TableViewHolder(binding, onTableClick, onTableLongClick, onAffectClick)
    }
    
    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class TableViewHolder(
        private val binding: ItemTableBinding,
        private val onTableClick: (TableEntity) -> Unit,
        private val onTableLongClick: (TableEntity) -> Unit,
        private val onAffectClick: (TableEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private var currentTable: TableEntity? = null

        init {
            binding.root.setOnClickListener {
                currentTable?.let { onTableClick(it) }
            }
            binding.root.setOnLongClickListener {
                currentTable?.let { onTableLongClick(it) }
                true
            }
        }

        fun bind(table: TableEntity) {
            currentTable = table
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
