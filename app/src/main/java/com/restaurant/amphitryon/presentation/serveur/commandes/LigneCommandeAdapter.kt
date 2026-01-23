package com.restaurant.amphitryon.presentation.serveur.commandes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restaurant.amphitryon.data.local.database.entities.LigneCommandeEntity
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.databinding.ItemLigneCommandeBinding
import com.restaurant.amphitryon.domain.model.EtatPlat

data class LigneCommandeWithPlat(
    val ligneCommande: LigneCommandeEntity,
    val plat: PlatEntity?
)

class LigneCommandeAdapter(
    private val onChangeEtatClick: (LigneCommandeEntity) -> Unit = {},
    private val onLigneClick: (LigneCommandeEntity) -> Unit = {}
) : ListAdapter<LigneCommandeWithPlat, LigneCommandeAdapter.LigneCommandeViewHolder>(LigneCommandeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigneCommandeViewHolder {
        val binding = ItemLigneCommandeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LigneCommandeViewHolder(binding, onChangeEtatClick, onLigneClick)
    }

    override fun onBindViewHolder(holder: LigneCommandeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LigneCommandeViewHolder(
        private val binding: ItemLigneCommandeBinding,
        private val onChangeEtatClick: (LigneCommandeEntity) -> Unit,
        private val onLigneClick: (LigneCommandeEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentLigne: LigneCommandeEntity? = null

        init {
            binding.root.setOnClickListener {
                currentLigne?.let { onLigneClick(it) }
            }
            binding.btnChangeEtat.setOnClickListener {
                currentLigne?.let { onChangeEtatClick(it) }
            }
        }

        fun bind(item: LigneCommandeWithPlat) {
            currentLigne = item.ligneCommande
            binding.tvPlatNom.text = item.plat?.nom ?: "Plat inconnu"
            binding.tvQuantite.text = "x${item.ligneCommande.quantite}"
            binding.tvInfoComplementaire.text = item.ligneCommande.informationsComplementaires.ifEmpty {
                "Pas d'informations complémentaires"
            }
            binding.tvEtat.text = when (item.ligneCommande.etat) {
                EtatPlat.COMMANDE -> "Commandé"
                EtatPlat.SERVI -> "Servi"
                EtatPlat.DEBARRASSE -> "Débarrassé"
            }

            // Change button text based on current state
            binding.btnChangeEtat.text = when (item.ligneCommande.etat) {
                EtatPlat.COMMANDE -> "Marquer Servi"
                EtatPlat.SERVI -> "Marquer Débarrassé"
                EtatPlat.DEBARRASSE -> "Terminé"
            }
            binding.btnChangeEtat.isEnabled = item.ligneCommande.etat != EtatPlat.DEBARRASSE
        }
    }

    private class LigneCommandeDiffCallback : DiffUtil.ItemCallback<LigneCommandeWithPlat>() {
        override fun areItemsTheSame(oldItem: LigneCommandeWithPlat, newItem: LigneCommandeWithPlat): Boolean {
            return oldItem.ligneCommande.id == newItem.ligneCommande.id
        }

        override fun areContentsTheSame(oldItem: LigneCommandeWithPlat, newItem: LigneCommandeWithPlat): Boolean {
            return oldItem == newItem
        }
    }
}
