package com.restaurant.amphitryon.presentation.chefsalle.affectations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.AffectationEntity
import com.restaurant.amphitryon.data.local.database.entities.ServeurEntity
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.databinding.ItemAffectationBinding
import com.restaurant.amphitryon.domain.model.Service

/**
 * Classe pour regrouper une affectation avec les détails de la table et du serveur
 */
data class AffectationWithDetails(
    val affectation: AffectationEntity,
    val table: TableEntity?,
    val serveur: ServeurEntity?
)

/**
 * Adapter pour afficher la liste des affectations dans un RecyclerView
 *
 * Utilise ListAdapter avec DiffUtil pour optimiser les mises à jour
 */
class AffectationAdapter(
    private val onAffectationClick: (AffectationWithDetails) -> Unit = {},      // Clic simple -> modifier
    private val onAffectationLongClick: (AffectationWithDetails) -> Unit = {},  // Appui long -> options
    private val onDeleteClick: (AffectationWithDetails) -> Unit = {}            // Bouton supprimer
) : ListAdapter<AffectationWithDetails, AffectationAdapter.AffectationViewHolder>(AffectationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AffectationViewHolder {
        val binding = ItemAffectationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AffectationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AffectationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder pour un élément affectation
     */
    inner class AffectationViewHolder(
        private val binding: ItemAffectationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AffectationWithDetails) {
            // Afficher les informations de l'affectation
            binding.tvTableNumero.text = item.table?.let { "Table ${it.numero}" } ?: "Table ?"
            binding.tvServeurNom.text = item.serveur?.let { "${it.prenom} ${it.nom}" } ?: "Serveur ?"
            binding.tvService.text = when (item.affectation.service) {
                Service.DEJEUNER -> "Déjeuner"
                Service.DINER -> "Dîner"
            }

            // Gérer les clics
            binding.root.setOnClickListener { onAffectationClick(item) }
            binding.root.setOnLongClickListener {
                onAffectationLongClick(item)
                true
            }
            binding.btnDeleteAffectation.setOnClickListener { onDeleteClick(item) }
        }
    }

    /**
     * DiffUtil pour comparer les affectations et optimiser les mises à jour
     */
    private class AffectationDiffCallback : DiffUtil.ItemCallback<AffectationWithDetails>() {
        override fun areItemsTheSame(oldItem: AffectationWithDetails, newItem: AffectationWithDetails): Boolean {
            return oldItem.affectation.id == newItem.affectation.id
        }

        override fun areContentsTheSame(oldItem: AffectationWithDetails, newItem: AffectationWithDetails): Boolean {
            return oldItem == newItem
        }
    }
}
