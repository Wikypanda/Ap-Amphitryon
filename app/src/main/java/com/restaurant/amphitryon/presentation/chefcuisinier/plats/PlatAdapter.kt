package com.restaurant.amphitryon.presentation.chefcuisinier.plats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.databinding.ItemPlatBinding
import com.restaurant.amphitryon.domain.model.CategoriePlat

class PlatAdapter(
    private val onPlatClick: (PlatEntity) -> Unit = {},
    private val onPlatLongClick: (PlatEntity) -> Unit = {},
    private val onProposeClick: (PlatEntity) -> Unit = {}
) : ListAdapter<PlatEntity, PlatAdapter.PlatViewHolder>(PlatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatViewHolder {
        val binding = ItemPlatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlatViewHolder(binding, onPlatClick, onPlatLongClick, onProposeClick)
    }
    
    override fun onBindViewHolder(holder: PlatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class PlatViewHolder(
        private val binding: ItemPlatBinding,
        private val onPlatClick: (PlatEntity) -> Unit,
        private val onPlatLongClick: (PlatEntity) -> Unit,
        private val onProposeClick: (PlatEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private var currentPlat: PlatEntity? = null

        init {
            binding.root.setOnClickListener {
                currentPlat?.let { onPlatClick(it) }
            }
            binding.root.setOnLongClickListener {
                currentPlat?.let { onPlatLongClick(it) }
                true
            }
        }

        fun bind(plat: PlatEntity) {
            currentPlat = plat
            binding.tvPlatNom.text = plat.nom
            binding.tvPlatCategorie.text = when (plat.categorie) {
                CategoriePlat.ENTREE -> "Entrée"
                CategoriePlat.PLAT_PRINCIPAL -> "Plat Principal"
                CategoriePlat.DESSERT -> "Dessert"
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
