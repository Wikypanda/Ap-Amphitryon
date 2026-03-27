package com.restaurant.amphitryon.presentation.chefsalle.tables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.databinding.ItemTableBinding

/**
 * Adapter pour afficher la liste des tables dans un RecyclerView
 *
 * Utilise ListAdapter avec DiffUtil pour optimiser les mises à jour
 */
class TableAdapter(
    private val onTableClick: (TableEntity) -> Unit = {},      // Clic simple -> modifier
    private val onTableLongClick: (TableEntity) -> Unit = {},  // Appui long -> options
    private val onDeleteClick: (TableEntity) -> Unit = {}      // Bouton supprimer
) : ListAdapter<TableEntity, TableAdapter.TableViewHolder>(TableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = ItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder pour un élément table
     */
    inner class TableViewHolder(
        private val binding: ItemTableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(table: TableEntity) {
            // Afficher les informations de la table
            binding.tvTableNumero.text = "Table ${table.numero}"
            binding.tvTableSeats.text = "${table.nombrePlaces} places"
            binding.tvTableStatus.text = if (table.estOccupee) "Occupée" else "Disponible"

            // Gérer les clics
            binding.root.setOnClickListener { onTableClick(table) }
            binding.root.setOnLongClickListener {
                onTableLongClick(table)
                true
            }
            binding.btnDeleteTable.setOnClickListener { onDeleteClick(table) }
        }
    }

    /**
     * DiffUtil pour comparer les tables et optimiser les mises à jour
     */
    private class TableDiffCallback : DiffUtil.ItemCallback<TableEntity>() {
        override fun areItemsTheSame(oldItem: TableEntity, newItem: TableEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TableEntity, newItem: TableEntity): Boolean {
            return oldItem == newItem
        }
    }
}
