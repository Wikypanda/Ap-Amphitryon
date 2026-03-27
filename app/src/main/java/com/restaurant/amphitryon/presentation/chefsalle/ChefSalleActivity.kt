package com.restaurant.amphitryon.presentation.chefsalle

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.restaurant.amphitryon.R
import com.restaurant.amphitryon.data.local.database.entities.AffectationEntity
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.databinding.ActivityChefSalleBinding
import com.restaurant.amphitryon.domain.model.Service
import com.restaurant.amphitryon.presentation.chefsalle.affectations.AffectationAdapter
import com.restaurant.amphitryon.presentation.chefsalle.affectations.AffectationWithDetails
import com.restaurant.amphitryon.presentation.chefsalle.tables.TableAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Activity pour le Chef de Salle
 *
 * Fonctionnalités :
 * - Gérer les tables (créer, modifier, supprimer, afficher)
 * - Affecter les tables aux serveurs
 * - Visualiser les affectations par date et service
 */
@AndroidEntryPoint
class ChefSalleActivity : AppCompatActivity() {

    // View Binding pour accéder aux éléments de l'interface
    private lateinit var binding: ActivityChefSalleBinding

    // ViewModel injecté par Hilt
    private val viewModel: ChefSalleViewModel by viewModels()

    // Adapters pour les RecyclerViews
    private lateinit var tableAdapter: TableAdapter
    private lateinit var affectationAdapter: AffectationAdapter

    // Date et service actuellement sélectionnés
    private var dateSelectionnee: String = ""
    private var serviceSelectionne: Service = Service.DEJEUNER

    // ==================== CYCLE DE VIE ====================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefSalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialiserDate()
        configurerToolbar()
        configurerRecyclerViews()
        configurerOnglets()
        configurerFiltres()
        observerDonnees()
        configurerBoutonAjouter()
    }

    // ==================== INITIALISATION ====================

    /**
     * Initialise la date du jour
     */
    private fun initialiserDate() {
        val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateSelectionnee = formatDate.format(Calendar.getInstance().time)
        binding.btnSelectDate.text = dateSelectionnee
        viewModel.setSelectedDate(dateSelectionnee)
    }

    /**
     * Configure la barre d'outils
     */
    private fun configurerToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    /**
     * Configure les RecyclerViews pour les tables et les affectations
     */
    private fun configurerRecyclerViews() {
        // Adapter pour la liste des tables
        tableAdapter = TableAdapter(
            onTableClick = { table -> afficherDialogModifierTable(table) },
            onTableLongClick = { table -> afficherOptionsTable(table) },
            onDeleteClick = { table -> confirmerSuppressionTable(table) }
        )
        binding.recyclerViewTables.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTables.adapter = tableAdapter

        // Adapter pour la liste des affectations
        affectationAdapter = AffectationAdapter(
            onAffectationClick = { affectation -> afficherDialogModifierAffectation(affectation) },
            onAffectationLongClick = { affectation -> afficherOptionsAffectation(affectation) },
            onDeleteClick = { affectation -> confirmerSuppressionAffectation(affectation) }
        )
        binding.recyclerViewAffectations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAffectations.adapter = affectationAdapter
    }

    /**
     * Configure les onglets (Tables / Affectations)
     */
    private fun configurerOnglets() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> { // Onglet Tables
                        binding.recyclerViewTables.visibility = View.VISIBLE
                        binding.recyclerViewAffectations.visibility = View.GONE
                    }
                    1 -> { // Onglet Affectations
                        binding.recyclerViewTables.visibility = View.GONE
                        binding.recyclerViewAffectations.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Configure les filtres (date et service)
     */
    private fun configurerFiltres() {
        // Bouton de sélection de date
        binding.btnSelectDate.setOnClickListener {
            afficherSelecteurDate { date ->
                dateSelectionnee = date
                binding.btnSelectDate.text = date
                viewModel.setSelectedDate(date)
            }
        }

        // Radio boutons pour le service
        binding.radioDejeuner.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                serviceSelectionne = Service.DEJEUNER
                viewModel.setSelectedService(Service.DEJEUNER)
            }
        }

        binding.radioDiner.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                serviceSelectionne = Service.DINER
                viewModel.setSelectedService(Service.DINER)
            }
        }
    }

    /**
     * Observe les données du ViewModel et met à jour l'interface
     */
    private fun observerDonnees() {
        // Observer les tables
        lifecycleScope.launch {
            viewModel.tables.collect { tables ->
                tableAdapter.submitList(tables)
            }
        }

        // Observer les affectations
        lifecycleScope.launch {
            viewModel.affectationsWithDetails.collect { affectations ->
                affectationAdapter.submitList(affectations)
            }
        }
    }

    /**
     * Configure le bouton flottant d'ajout
     */
    private fun configurerBoutonAjouter() {
        binding.fabAddTable.setOnClickListener {
            if (binding.tabLayout.selectedTabPosition == 0) {
                afficherDialogAjouterTable()
            } else {
                afficherDialogNouvelleAffectation()
            }
        }
    }

    // ==================== UTILITAIRES ====================

    /**
     * Affiche un sélecteur de date
     */
    private fun afficherSelecteurDate(onDateSelected: (String) -> Unit) {
        val calendrier = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, annee, mois, jour ->
                val date = String.format(Locale.getDefault(), "%04d-%02d-%02d", annee, mois + 1, jour)
                onDateSelected(date)
            },
            calendrier.get(Calendar.YEAR),
            calendrier.get(Calendar.MONTH),
            calendrier.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /**
     * Affiche un message Toast
     */
    private fun afficherMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // ==================== GESTION DES TABLES ====================

    /**
     * Affiche le dialogue pour ajouter une table
     */
    private fun afficherDialogAjouterTable() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_table, null)
        val etNumero = dialogView.findViewById<TextInputEditText>(R.id.etTableNumero)
        val etPlaces = dialogView.findViewById<TextInputEditText>(R.id.etTablePlaces)

        MaterialAlertDialogBuilder(this)
            .setTitle("Ajouter une table")
            .setView(dialogView)
            .setPositiveButton("Enregistrer") { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val places = etPlaces.text.toString().toIntOrNull()

                if (numero != null && places != null && places >= 2) {
                    val nouvelleTable = TableEntity(numero = numero, nombrePlaces = places)
                    viewModel.ajouterTable(nouvelleTable)
                    afficherMessage("Table ajoutée")
                } else {
                    afficherMessage("Erreur : numéro invalide ou moins de 2 places")
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    /**
     * Affiche le dialogue pour modifier une table
     */
    private fun afficherDialogModifierTable(table: TableEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_table, null)
        val etNumero = dialogView.findViewById<TextInputEditText>(R.id.etTableNumero)
        val etPlaces = dialogView.findViewById<TextInputEditText>(R.id.etTablePlaces)

        // Pré-remplir avec les valeurs actuelles
        etNumero.setText(table.numero.toString())
        etPlaces.setText(table.nombrePlaces.toString())

        MaterialAlertDialogBuilder(this)
            .setTitle("Modifier la table ${table.numero}")
            .setView(dialogView)
            .setPositiveButton("Enregistrer") { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val places = etPlaces.text.toString().toIntOrNull()

                if (numero != null && places != null && places >= 2) {
                    val tableModifiee = table.copy(numero = numero, nombrePlaces = places)
                    viewModel.modifierTable(tableModifiee)
                    afficherMessage("Table modifiée")
                } else {
                    afficherMessage("Erreur : numéro invalide ou moins de 2 places")
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    /**
     * Affiche les options pour une table (appui long)
     */
    private fun afficherOptionsTable(table: TableEntity) {
        val options = arrayOf("Modifier", "Supprimer", "Affecter à un serveur")
        MaterialAlertDialogBuilder(this)
            .setTitle("Table ${table.numero}")
            .setItems(options) { _, choix ->
                when (choix) {
                    0 -> afficherDialogModifierTable(table)
                    1 -> confirmerSuppressionTable(table)
                    2 -> afficherDialogAffecterTable(table)
                }
            }
            .show()
    }

    /**
     * Demande confirmation avant de supprimer une table
     */
    private fun confirmerSuppressionTable(table: TableEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Supprimer la table")
            .setMessage("Voulez-vous vraiment supprimer la table ${table.numero} ?")
            .setPositiveButton("Supprimer") { _, _ ->
                viewModel.supprimerTable(table)
                afficherMessage("Table supprimée")
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    // ==================== GESTION DES AFFECTATIONS ====================

    /**
     * Affiche le dialogue pour créer une nouvelle affectation
     * (depuis l'onglet Affectations)
     */
    private fun afficherDialogNouvelleAffectation() {
        lifecycleScope.launch {
            val tables = viewModel.tables.first()

            if (tables.isEmpty()) {
                afficherMessage("Aucune table disponible")
                return@launch
            }

            // Afficher la liste des tables à sélectionner
            val nomsDesTables = tables.map { "Table ${it.numero} (${it.nombrePlaces} places)" }

            MaterialAlertDialogBuilder(this@ChefSalleActivity)
                .setTitle("Choisir une table")
                .setItems(nomsDesTables.toTypedArray()) { _, index ->
                    afficherDialogAffecterTable(tables[index])
                }
                .setNegativeButton("Annuler", null)
                .show()
        }
    }

    /**
     * Affiche le dialogue pour affecter une table à un serveur
     */
    private fun afficherDialogAffecterTable(table: TableEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_affectation, null)
        val spinnerServeur = dialogView.findViewById<Spinner>(R.id.spinnerServeur)
        val radioDejeuner = dialogView.findViewById<RadioButton>(R.id.radioDejeuner)
        val radioDiner = dialogView.findViewById<RadioButton>(R.id.radioDiner)
        val btnDate = dialogView.findViewById<MaterialButton>(R.id.btnSelectDate)

        // Date par défaut
        var dateAffectation = dateSelectionnee
        btnDate.text = dateAffectation

        // Service par défaut
        if (serviceSelectionne == Service.DEJEUNER) {
            radioDejeuner.isChecked = true
        } else {
            radioDiner.isChecked = true
        }

        lifecycleScope.launch {
            val serveurs = viewModel.serveurs.first()

            if (serveurs.isEmpty()) {
                afficherMessage("Aucun serveur disponible")
                return@launch
            }

            // Remplir le spinner avec les serveurs
            val nomsServeurs = serveurs.map { "${it.prenom} ${it.nom}" }
            val adapter = ArrayAdapter(this@ChefSalleActivity, android.R.layout.simple_spinner_item, nomsServeurs)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerServeur.adapter = adapter

            // Sélecteur de date
            btnDate.setOnClickListener {
                afficherSelecteurDate { date ->
                    dateAffectation = date
                    btnDate.text = date
                }
            }

            MaterialAlertDialogBuilder(this@ChefSalleActivity)
                .setTitle("Affecter la table ${table.numero}")
                .setView(dialogView)
                .setPositiveButton("Enregistrer") { _, _ ->
                    val serveurChoisi = serveurs[spinnerServeur.selectedItemPosition]
                    val service = if (radioDejeuner.isChecked) Service.DEJEUNER else Service.DINER

                    val affectation = AffectationEntity(
                        tableId = table.id,
                        serveurId = serveurChoisi.id,
                        date = dateAffectation,
                        service = service
                    )
                    viewModel.ajouterAffectation(affectation)
                    afficherMessage("Affectation créée")
                }
                .setNegativeButton("Annuler", null)
                .show()
        }
    }

    /**
     * Affiche le dialogue pour modifier une affectation
     */
    private fun afficherDialogModifierAffectation(affectationDetails: AffectationWithDetails) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_affectation, null)
        val tvTableLabel = dialogView.findViewById<TextView>(R.id.tvTableLabel)
        val spinnerTable = dialogView.findViewById<Spinner>(R.id.spinnerTable)
        val spinnerServeur = dialogView.findViewById<Spinner>(R.id.spinnerServeur)
        val radioDejeuner = dialogView.findViewById<RadioButton>(R.id.radioDejeuner)
        val radioDiner = dialogView.findViewById<RadioButton>(R.id.radioDiner)
        val btnDate = dialogView.findViewById<MaterialButton>(R.id.btnSelectDate)

        val affectation = affectationDetails.affectation

        // Afficher le sélecteur de table (visible seulement en modification)
        tvTableLabel.visibility = View.VISIBLE
        spinnerTable.visibility = View.VISIBLE

        // Date actuelle
        var dateAffectation = affectation.date
        btnDate.text = dateAffectation

        // Service actuel
        if (affectation.service == Service.DEJEUNER) {
            radioDejeuner.isChecked = true
        } else {
            radioDiner.isChecked = true
        }

        lifecycleScope.launch {
            val serveurs = viewModel.serveurs.first()
            val tables = viewModel.tables.first()

            if (serveurs.isEmpty() || tables.isEmpty()) {
                afficherMessage("Données manquantes")
                return@launch
            }

            // Remplir le spinner des tables
            val nomsTables = tables.map { "Table ${it.numero} (${it.nombrePlaces} places)" }
            val tableAdapter = ArrayAdapter(this@ChefSalleActivity, android.R.layout.simple_spinner_item, nomsTables)
            tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTable.adapter = tableAdapter

            // Sélectionner la table actuelle
            val indexTable = tables.indexOfFirst { it.id == affectation.tableId }
            if (indexTable >= 0) spinnerTable.setSelection(indexTable)

            // Remplir le spinner des serveurs
            val nomsServeurs = serveurs.map { "${it.prenom} ${it.nom}" }
            val serveurAdapter = ArrayAdapter(this@ChefSalleActivity, android.R.layout.simple_spinner_item, nomsServeurs)
            serveurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerServeur.adapter = serveurAdapter

            // Sélectionner le serveur actuel
            val indexServeur = serveurs.indexOfFirst { it.id == affectation.serveurId }
            if (indexServeur >= 0) spinnerServeur.setSelection(indexServeur)

            // Sélecteur de date
            btnDate.setOnClickListener {
                afficherSelecteurDate { date ->
                    dateAffectation = date
                    btnDate.text = date
                }
            }

            MaterialAlertDialogBuilder(this@ChefSalleActivity)
                .setTitle("Modifier l'affectation")
                .setView(dialogView)
                .setPositiveButton("Enregistrer") { _, _ ->
                    val tableChoisie = tables[spinnerTable.selectedItemPosition]
                    val serveurChoisi = serveurs[spinnerServeur.selectedItemPosition]
                    val service = if (radioDejeuner.isChecked) Service.DEJEUNER else Service.DINER

                    val affectationModifiee = affectation.copy(
                        tableId = tableChoisie.id,
                        serveurId = serveurChoisi.id,
                        date = dateAffectation,
                        service = service
                    )
                    viewModel.modifierAffectation(affectationModifiee)
                    afficherMessage("Affectation modifiée")
                }
                .setNegativeButton("Annuler", null)
                .show()
        }
    }

    /**
     * Affiche les options pour une affectation (appui long)
     */
    private fun afficherOptionsAffectation(affectation: AffectationWithDetails) {
        val nomTable = affectation.table?.let { "Table ${it.numero}" } ?: "Table ?"
        val nomServeur = affectation.serveur?.let { "${it.prenom} ${it.nom}" } ?: "Serveur ?"

        val options = arrayOf("Modifier", "Supprimer")
        MaterialAlertDialogBuilder(this)
            .setTitle("$nomTable → $nomServeur")
            .setItems(options) { _, choix ->
                when (choix) {
                    0 -> afficherDialogModifierAffectation(affectation)
                    1 -> confirmerSuppressionAffectation(affectation)
                }
            }
            .show()
    }

    /**
     * Demande confirmation avant de supprimer une affectation
     */
    private fun confirmerSuppressionAffectation(affectation: AffectationWithDetails) {
        val nomTable = affectation.table?.let { "Table ${it.numero}" } ?: "Table ?"
        val nomServeur = affectation.serveur?.let { "${it.prenom} ${it.nom}" } ?: "Serveur ?"

        MaterialAlertDialogBuilder(this)
            .setTitle("Supprimer l'affectation")
            .setMessage("Voulez-vous vraiment supprimer l'affectation de $nomTable à $nomServeur ?")
            .setPositiveButton("Supprimer") { _, _ ->
                viewModel.supprimerAffectation(affectation.affectation)
                afficherMessage("Affectation supprimée")
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}
