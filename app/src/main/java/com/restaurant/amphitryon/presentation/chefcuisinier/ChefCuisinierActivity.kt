package com.restaurant.amphitryon.presentation.chefcuisinier

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.restaurant.amphitryon.R
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.data.local.database.entities.PlatServiceEntity
import com.restaurant.amphitryon.databinding.ActivityChefCuisinierBinding
import com.restaurant.amphitryon.domain.model.CategoriePlat
import com.restaurant.amphitryon.domain.model.Service
import com.restaurant.amphitryon.presentation.chefcuisinier.plats.PlatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ChefCuisinierActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityChefCuisinierBinding
    private val viewModel: ChefCuisinierViewModel by viewModels()
    private lateinit var platAdapter: PlatAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefCuisinierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        observePlats()
        setupClickListeners()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        platAdapter = PlatAdapter(
            onPlatClick = { plat -> showEditPlatDialog(plat) },
            onPlatLongClick = { plat -> showPlatOptionsDialog(plat) },
            onProposeClick = { plat -> showPlatServiceDialog(plat) }
        )
        binding.recyclerViewPlats.apply {
            layoutManager = LinearLayoutManager(this@ChefCuisinierActivity)
            adapter = platAdapter
        }
    }
    
    private fun observePlats() {
        lifecycleScope.launch {
            viewModel.plats.collect { plats ->
                platAdapter.submitList(plats)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddPlat.setOnClickListener {
            showAddPlatDialog()
        }
    }

    private fun showAddPlatDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_plat, null)
        val etNumero = dialogView.findViewById<TextInputEditText>(R.id.etPlatNumero)
        val etNom = dialogView.findViewById<TextInputEditText>(R.id.etPlatNom)
        val etDescriptif = dialogView.findViewById<TextInputEditText>(R.id.etPlatDescriptif)
        val radioEntree = dialogView.findViewById<RadioButton>(R.id.radioEntree)
        val radioPlatPrincipal = dialogView.findViewById<RadioButton>(R.id.radioPlatPrincipal)
        val radioDessert = dialogView.findViewById<RadioButton>(R.id.radioDessert)
        val etPrix = dialogView.findViewById<TextInputEditText>(R.id.etPlatPrix)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.add_dish)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val nom = etNom.text.toString()
                val descriptif = etDescriptif.text.toString()
                val prix = etPrix.text.toString().toDoubleOrNull()

                val categorie = when {
                    radioEntree.isChecked -> CategoriePlat.ENTREE
                    radioPlatPrincipal.isChecked -> CategoriePlat.PLAT_PRINCIPAL
                    else -> CategoriePlat.DESSERT
                }

                if (numero != null && nom.isNotBlank() && prix != null) {
                    viewModel.insertPlat(
                        PlatEntity(
                            numero = numero,
                            nom = nom,
                            descriptif = descriptif,
                            categorie = categorie,
                            prixBase = prix
                        )
                    )
                    Toast.makeText(this, "Plat ajouté", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showEditPlatDialog(plat: PlatEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_plat, null)
        val etNumero = dialogView.findViewById<TextInputEditText>(R.id.etPlatNumero)
        val etNom = dialogView.findViewById<TextInputEditText>(R.id.etPlatNom)
        val etDescriptif = dialogView.findViewById<TextInputEditText>(R.id.etPlatDescriptif)
        val radioEntree = dialogView.findViewById<RadioButton>(R.id.radioEntree)
        val radioPlatPrincipal = dialogView.findViewById<RadioButton>(R.id.radioPlatPrincipal)
        val radioDessert = dialogView.findViewById<RadioButton>(R.id.radioDessert)
        val etPrix = dialogView.findViewById<TextInputEditText>(R.id.etPlatPrix)

        etNumero.setText(plat.numero.toString())
        etNom.setText(plat.nom)
        etDescriptif.setText(plat.descriptif)
        etPrix.setText(plat.prixBase.toString())

        when (plat.categorie) {
            CategoriePlat.ENTREE -> radioEntree.isChecked = true
            CategoriePlat.PLAT_PRINCIPAL -> radioPlatPrincipal.isChecked = true
            CategoriePlat.DESSERT -> radioDessert.isChecked = true
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.edit_dish)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val nom = etNom.text.toString()
                val descriptif = etDescriptif.text.toString()
                val prix = etPrix.text.toString().toDoubleOrNull()

                val categorie = when {
                    radioEntree.isChecked -> CategoriePlat.ENTREE
                    radioPlatPrincipal.isChecked -> CategoriePlat.PLAT_PRINCIPAL
                    else -> CategoriePlat.DESSERT
                }

                if (numero != null && nom.isNotBlank() && prix != null) {
                    viewModel.updatePlat(
                        plat.copy(
                            numero = numero,
                            nom = nom,
                            descriptif = descriptif,
                            categorie = categorie,
                            prixBase = prix
                        )
                    )
                    Toast.makeText(this, "Plat modifié", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showPlatOptionsDialog(plat: PlatEntity) {
        val options = arrayOf("Modifier", "Supprimer", "Proposer pour un service")
        MaterialAlertDialogBuilder(this)
            .setTitle(plat.nom)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditPlatDialog(plat)
                    1 -> showDeletePlatConfirmation(plat)
                    2 -> showPlatServiceDialog(plat)
                }
            }
            .show()
    }

    private fun showDeletePlatConfirmation(plat: PlatEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_dish)
            .setMessage("Êtes-vous sûr de vouloir supprimer ${plat.nom} ?")
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deletePlat(plat)
                Toast.makeText(this, "Plat supprimé", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showPlatServiceDialog(plat: PlatEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_plat_service, null)
        val radioDejeuner = dialogView.findViewById<RadioButton>(R.id.radioDejeuner)
        val btnSelectDate = dialogView.findViewById<MaterialButton>(R.id.btnSelectDate)
        val etPrixVente = dialogView.findViewById<TextInputEditText>(R.id.etPrixVente)
        val etQuantiteDisponible = dialogView.findViewById<TextInputEditText>(R.id.etQuantiteDisponible)
        val switchPropose = dialogView.findViewById<SwitchMaterial>(R.id.switchPropose)

        var selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        btnSelectDate.text = selectedDate
        etPrixVente.setText(plat.prixBase.toString())

        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
                    btnSelectDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.propose_for_service)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val prixVente = etPrixVente.text.toString().toDoubleOrNull() ?: plat.prixBase
                val quantite = etQuantiteDisponible.text.toString().toIntOrNull() ?: 0
                val service = if (radioDejeuner.isChecked) Service.DEJEUNER else Service.DINER

                viewModel.insertPlatService(
                    PlatServiceEntity(
                        platId = plat.id,
                        date = selectedDate,
                        service = service,
                        prixVente = prixVente,
                        quantiteDisponible = quantite,
                        estPropose = switchPropose.isChecked
                    )
                )
                Toast.makeText(this, "Plat proposé pour le service", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
