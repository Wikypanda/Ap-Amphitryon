package com.restaurant.amphitryon.presentation.serveur

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.restaurant.amphitryon.R
import com.restaurant.amphitryon.data.local.database.entities.CommandeEntity
import com.restaurant.amphitryon.data.local.database.entities.LigneCommandeEntity
import com.restaurant.amphitryon.databinding.ActivityServeurBinding
import com.restaurant.amphitryon.domain.model.EtatPlat
import com.restaurant.amphitryon.domain.model.Service
import com.restaurant.amphitryon.presentation.serveur.commandes.CommandeAdapter
import com.restaurant.amphitryon.presentation.serveur.commandes.LigneCommandeWithPlat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ServeurActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityServeurBinding
    private val viewModel: ServeurViewModel by viewModels()
    private lateinit var commandeAdapter: CommandeAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServeurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        observeCommandes()
        setupClickListeners()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        commandeAdapter = CommandeAdapter(
            onCommandeClick = { commande -> showCommandeDetailsDialog(commande) },
            onCommandeLongClick = { commande -> showCommandeOptionsDialog(commande) },
            onPayClick = { commande -> showPaymentConfirmation(commande) }
        )
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
            showAddCommandeDialog()
        }
    }

    private fun showAddCommandeDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_commande, null)
        val spinnerTable = dialogView.findViewById<Spinner>(R.id.spinnerTable)
        val radioDejeuner = dialogView.findViewById<RadioButton>(R.id.radioDejeuner)

        lifecycleScope.launch {
            val tables = viewModel.tables.first()
            val tableNames = tables.map { "Table ${it.numero}" }
            val adapter = ArrayAdapter(this@ServeurActivity, android.R.layout.simple_spinner_item, tableNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTable.adapter = adapter

            MaterialAlertDialogBuilder(this@ServeurActivity)
                .setTitle(R.string.add_order)
                .setView(dialogView)
                .setPositiveButton(R.string.save) { _, _ ->
                    if (tables.isNotEmpty()) {
                        val selectedTable = tables[spinnerTable.selectedItemPosition]
                        val service = if (radioDejeuner.isChecked) Service.DEJEUNER else Service.DINER
                        val dateHeure = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .format(Calendar.getInstance().time)

                        viewModel.insertCommande(
                            CommandeEntity(
                                tableId = selectedTable.id,
                                dateHeure = dateHeure,
                                service = service
                            )
                        )
                        Toast.makeText(this@ServeurActivity, "Commande créée", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun showCommandeOptionsDialog(commande: CommandeEntity) {
        val options = mutableListOf("Voir détails", "Ajouter un plat", "Supprimer")
        if (!commande.estReglee) {
            options.add("Marquer comme payée")
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Commande #${commande.id}")
            .setItems(options.toTypedArray()) { _, which ->
                when (which) {
                    0 -> showCommandeDetailsDialog(commande)
                    1 -> showAddLigneCommandeDialog(commande)
                    2 -> showDeleteCommandeConfirmation(commande)
                    3 -> showPaymentConfirmation(commande)
                }
            }
            .show()
    }

    private fun showCommandeDetailsDialog(commande: CommandeEntity) {
        lifecycleScope.launch {
            val lignes = viewModel.getLignesCommandeForCommande(commande.id)
            val plats = viewModel.plats.first()

            val lignesWithPlats = lignes.map { ligne ->
                LigneCommandeWithPlat(ligne, plats.find { it.id == ligne.platId })
            }

            if (lignesWithPlats.isEmpty()) {
                MaterialAlertDialogBuilder(this@ServeurActivity)
                    .setTitle("Commande #${commande.id}")
                    .setMessage("Aucun plat dans cette commande.\n\nVoulez-vous ajouter un plat ?")
                    .setPositiveButton("Ajouter un plat") { _, _ ->
                        showAddLigneCommandeDialog(commande)
                    }
                    .setNegativeButton("Fermer", null)
                    .show()
            } else {
                val detailsBuilder = StringBuilder()
                detailsBuilder.append("Table: ${commande.tableId}\n")
                detailsBuilder.append("Date: ${commande.dateHeure}\n")
                detailsBuilder.append("Service: ${if (commande.service == Service.DEJEUNER) "Déjeuner" else "Dîner"}\n")
                detailsBuilder.append("Statut: ${if (commande.estReglee) "Réglée" else "Non réglée"}\n\n")
                detailsBuilder.append("--- Plats ---\n")

                lignesWithPlats.forEach { item ->
                    val platNom = item.plat?.nom ?: "Plat inconnu"
                    val etat = when (item.ligneCommande.etat) {
                        EtatPlat.COMMANDE -> "Commandé"
                        EtatPlat.SERVI -> "Servi"
                        EtatPlat.DEBARRASSE -> "Débarrassé"
                    }
                    detailsBuilder.append("• $platNom x${item.ligneCommande.quantite} - $etat\n")
                    if (item.ligneCommande.informationsComplementaires.isNotEmpty()) {
                        detailsBuilder.append("  (${item.ligneCommande.informationsComplementaires})\n")
                    }
                }

                MaterialAlertDialogBuilder(this@ServeurActivity)
                    .setTitle("Commande #${commande.id}")
                    .setMessage(detailsBuilder.toString())
                    .setPositiveButton("Ajouter un plat") { _, _ ->
                        showAddLigneCommandeDialog(commande)
                    }
                    .setNeutralButton("Gérer états") { _, _ ->
                        showManageEtatsDialog(commande, lignesWithPlats)
                    }
                    .setNegativeButton("Fermer", null)
                    .show()
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun showManageEtatsDialog(commande: CommandeEntity, lignes: List<LigneCommandeWithPlat>) {
        val platNames = lignes.map {
            val platNom = it.plat?.nom ?: "Plat inconnu"
            val etat = when (it.ligneCommande.etat) {
                EtatPlat.COMMANDE -> "Commandé"
                EtatPlat.SERVI -> "Servi"
                EtatPlat.DEBARRASSE -> "Débarrassé"
            }
            "$platNom - $etat"
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Modifier l'état d'un plat")
            .setItems(platNames.toTypedArray()) { _, which ->
                val ligne = lignes[which].ligneCommande
                val nextEtat = when (ligne.etat) {
                    EtatPlat.COMMANDE -> EtatPlat.SERVI
                    EtatPlat.SERVI -> EtatPlat.DEBARRASSE
                    EtatPlat.DEBARRASSE -> EtatPlat.DEBARRASSE
                }
                if (ligne.etat != EtatPlat.DEBARRASSE) {
                    viewModel.updateLigneCommandeEtat(ligne.id, nextEtat)
                    Toast.makeText(this, "État mis à jour", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun showAddLigneCommandeDialog(commande: CommandeEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_ligne_commande, null)
        val spinnerPlat = dialogView.findViewById<Spinner>(R.id.spinnerPlat)
        val etQuantite = dialogView.findViewById<TextInputEditText>(R.id.etQuantite)
        val etInfo = dialogView.findViewById<TextInputEditText>(R.id.etInfoComplementaire)

        lifecycleScope.launch {
            val plats = viewModel.plats.first()
            val platNames = plats.map { "${it.nom} (${String.format(Locale.getDefault(), "%.2f €", it.prixBase)})" }
            val adapter = ArrayAdapter(this@ServeurActivity, android.R.layout.simple_spinner_item, platNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPlat.adapter = adapter

            MaterialAlertDialogBuilder(this@ServeurActivity)
                .setTitle(R.string.add_dish_to_order)
                .setView(dialogView)
                .setPositiveButton(R.string.save) { _, _ ->
                    if (plats.isNotEmpty()) {
                        val selectedPlat = plats[spinnerPlat.selectedItemPosition]
                        val quantite = etQuantite.text.toString().toIntOrNull() ?: 1
                        val info = etInfo.text.toString()

                        viewModel.insertLigneCommande(
                            LigneCommandeEntity(
                                commandeId = commande.id,
                                platId = selectedPlat.id,
                                quantite = quantite,
                                informationsComplementaires = info
                            )
                        )
                        Toast.makeText(this@ServeurActivity, "Plat ajouté à la commande", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun showDeleteCommandeConfirmation(commande: CommandeEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_order)
            .setMessage("Êtes-vous sûr de vouloir supprimer cette commande ?")
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteCommande(commande)
                Toast.makeText(this, "Commande supprimée", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showPaymentConfirmation(commande: CommandeEntity) {
        if (commande.estReglee) {
            Toast.makeText(this, "Cette commande est déjà réglée", Toast.LENGTH_SHORT).show()
            return
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.mark_as_paid)
            .setMessage("Confirmer le paiement de cette commande ?")
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.markCommandeAsPaid(commande.id)
                Toast.makeText(this, "Commande marquée comme payée", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
