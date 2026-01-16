package com.restaurant.amphitryon.presentation.serveur.commandes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.CommandeEntity
import com.restaurant.amphitryon.databinding.ItemCommandeBinding

class CommandeAdapter : ListAdapter<CommandeEntity, CommandeAdapter.CommandeViewHolder>(CommandeDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandeViewHolder {
        val binding = ItemCommandeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommandeViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CommandeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class CommandeViewHolder(
        private val binding: ItemCommandeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(commande: CommandeEntity) {
            binding.tvCommandeTable.text = "Table ${commande.tableId}"
            binding.tvCommandeDateTime.text = commande.dateHeure
            binding.tvCommandeService.text = when (commande.service) {
                com.restaurant.amphitryon.domain.model.Service.DEJEUNER -> "Déjeuner"
                com.restaurant.amphitryon.domain.model.Service.DINER -> "Dîner"
            }
            binding.tvCommandeStatus.text = if (commande.estReglee) "Réglée" else "Non réglée"
        }
    }
    
    private class CommandeDiffCallback : DiffUtil.ItemCallback<CommandeEntity>() {
        override fun areItemsTheSame(oldItem: CommandeEntity, newItem: CommandeEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: CommandeEntity, newItem: CommandeEntity): Boolean {
            return oldItem == newItem
        }
    }
}
