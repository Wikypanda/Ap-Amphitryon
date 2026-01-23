package com.restaurant.amphitryon.presentation.chefsalle

import android.app.DatePickerDialog
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.restaurant.amphitryon.R
import com.restaurant.amphitryon.data.local.database.entities.AffectationEntity
import com.restaurant.amphitryon.data.local.database.entities.ServeurEntity
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.databinding.ActivityChefSalleBinding
import com.restaurant.amphitryon.domain.model.Service
import com.restaurant.amphitryon.presentation.chefsalle.tables.TableAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ChefSalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChefSalleBinding
    private val viewModel: ChefSalleViewModel by viewModels()
    private lateinit var tableAdapter: TableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefSalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeTables()
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        tableAdapter = TableAdapter(
            onTableClick = { table -> showEditTableDialog(table) },
            onTableLongClick = { table -> showTableOptionsDialog(table) },
            onAffectClick = { table -> showAffectationDialog(table) }
        )
        binding.recyclerViewTables.apply {
            layoutManager = LinearLayoutManager(this@ChefSalleActivity)
            adapter = tableAdapter
        }
    }

    private fun observeTables() {
        lifecycleScope.launch {
            viewModel.tables.collect { tables ->
                tableAdapter.submitList(tables)
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTable.setOnClickListener {
            showAddTableDialog()
        }
    }

    private fun showAddTableDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_table, null)
        val etNumero = dialogView.findViewById<TextInputEditText>(R.id.etTableNumero)
        val etPlaces = dialogView.findViewById<TextInputEditText>(R.id.etTablePlaces)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.add_table)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val places = etPlaces.text.toString().toIntOrNull()

                if (numero != null && places != null && places >= 2) {
                    viewModel.insertTable(TableEntity(numero = numero, nombrePlaces = places))
                    Toast.makeText(this, "Table ajoutée", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Veuillez entrer des valeurs valides (min 2 places)", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showEditTableDialog(table: TableEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_table, null)
        val etNumero = dialogView.findViewById<TextInputEditText>(R.id.etTableNumero)
        val etPlaces = dialogView.findViewById<TextInputEditText>(R.id.etTablePlaces)

        etNumero.setText(table.numero.toString())
        etPlaces.setText(table.nombrePlaces.toString())

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.edit_table)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val places = etPlaces.text.toString().toIntOrNull()

                if (numero != null && places != null && places >= 2) {
                    viewModel.updateTable(table.copy(numero = numero, nombrePlaces = places))
                    Toast.makeText(this, "Table modifiée", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Veuillez entrer des valeurs valides (min 2 places)", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showTableOptionsDialog(table: TableEntity) {
        val options = arrayOf("Modifier", "Supprimer", "Affecter à un serveur")
        MaterialAlertDialogBuilder(this)
            .setTitle("Table ${table.numero}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditTableDialog(table)
                    1 -> showDeleteTableConfirmation(table)
                    2 -> showAffectationDialog(table)
                }
            }
            .show()
    }

    private fun showDeleteTableConfirmation(table: TableEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_table)
            .setMessage("Êtes-vous sûr de vouloir supprimer la table ${table.numero} ?")
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteTable(table)
                Toast.makeText(this, "Table supprimée", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showAffectationDialog(table: TableEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_affectation, null)
        val spinnerServeur = dialogView.findViewById<Spinner>(R.id.spinnerServeur)
        val radioDejeuner = dialogView.findViewById<RadioButton>(R.id.radioDejeuner)
        val btnSelectDate = dialogView.findViewById<MaterialButton>(R.id.btnSelectDate)

        var selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        btnSelectDate.text = selectedDate

        lifecycleScope.launch {
            val serveursList: List<ServeurEntity> = viewModel.serveurs.first()
            val serveurNames: List<String> = serveursList.map { serveur -> "${serveur.prenom} ${serveur.nom}" }
            val spinnerAdapter = ArrayAdapter(this@ChefSalleActivity, android.R.layout.simple_spinner_item, serveurNames)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerServeur.adapter = spinnerAdapter

            btnSelectDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    this@ChefSalleActivity,
                    { _, year, month, day ->
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
                        btnSelectDate.text = selectedDate
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            MaterialAlertDialogBuilder(this@ChefSalleActivity)
                .setTitle(R.string.assign_table)
                .setView(dialogView)
                .setPositiveButton(R.string.save) { _, _ ->
                    if (serveursList.isNotEmpty()) {
                        val selectedServeur: ServeurEntity = serveursList[spinnerServeur.selectedItemPosition]
                        val service: Service = if (radioDejeuner.isChecked) Service.DEJEUNER else Service.DINER

                        viewModel.insertAffectation(
                            AffectationEntity(
                                tableId = table.id,
                                serveurId = selectedServeur.id,
                                date = selectedDate,
                                service = service
                            )
                        )
                        Toast.makeText(this@ChefSalleActivity, "Affectation créée", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }
}
