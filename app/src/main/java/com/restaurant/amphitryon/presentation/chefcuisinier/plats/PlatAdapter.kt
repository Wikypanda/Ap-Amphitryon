package com.restaurant.amphitryon.presentation.chefcuisinier.plats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.databinding.ItemPlatBinding

class PlatAdapter : ListAdapter<PlatEntity, PlatAdapter.PlatViewHolder>(PlatDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatViewHolder {
        val binding = ItemPlatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlatViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PlatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class PlatViewHolder(
        private val binding: ItemPlatBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(plat: PlatEntity) {
            binding.tvPlatNom.text = plat.nom
            binding.tvPlatCategorie.text = when (plat.categorie) {
                com.restaurant.amphitryon.domain.model.CategoriePlat.ENTREE -> "Entrée"
                com.restaurant.amphitryon.domain.model.CategoriePlat.PLAT_PRINCIPAL -> "Plat Principal"
                com.restaurant.amphitryon.domain.model.CategoriePlat.DESSERT -> "Dessert"
            }
            binding.tvPlatDescription.text = plat.descriptif
            binding.tvPlatPrix.text = String.format("%.2f €", plat.prixBase)
        }
    }
    
    private class PlatDiffCallback : DiffUtil.ItemCallback<PlatEntity>() {
        override fun areItemsTheSame(oldItem: PlatEntity, newItem: PlatEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: PlatEntity, newItem: PlatEntity): Boolean {
            return oldItem == newItem
        }
    }
}
