package com.restaurant.amphitryon.presentation.chefsalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.amphitryon.data.local.database.entities.AffectationEntity
import com.restaurant.amphitryon.data.local.database.entities.ServeurEntity
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.data.repository.AffectationRepository
import com.restaurant.amphitryon.data.repository.ServeurRepository
import com.restaurant.amphitryon.data.repository.TableRepository
import com.restaurant.amphitryon.domain.model.Service
import com.restaurant.amphitryon.presentation.chefsalle.affectations.AffectationWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pour l'écran Chef de Salle
 *
 * Gère la logique métier pour :
 * - Les tables du restaurant (CRUD)
 * - Les affectations des tables aux serveurs
 * - Le filtrage par date et service (midi/soir)
 */
@HiltViewModel
class ChefSalleViewModel @Inject constructor(
    private val tableRepository: TableRepository,
    private val affectationRepository: AffectationRepository,
    private val serveurRepository: ServeurRepository
) : ViewModel() {

    // ==================== ÉTATS (StateFlow) ====================

    // Liste des tables
    private val _tables = MutableStateFlow<List<TableEntity>>(emptyList())
    val tables: StateFlow<List<TableEntity>> = _tables

    // Liste des serveurs
    private val _serveurs = MutableStateFlow<List<ServeurEntity>>(emptyList())
    val serveurs: StateFlow<List<ServeurEntity>> = _serveurs

    // Liste des affectations avec détails (table + serveur)
    private val _affectationsWithDetails = MutableStateFlow<List<AffectationWithDetails>>(emptyList())
    val affectationsWithDetails: StateFlow<List<AffectationWithDetails>> = _affectationsWithDetails

    // Filtres : date et service sélectionnés
    private var currentDate: String = ""
    private var currentService: Service = Service.DEJEUNER

    // ==================== INITIALISATION ====================

    init {
        chargerTables()
        chargerServeurs()
    }

    private fun chargerTables() {
        viewModelScope.launch {
            tableRepository.getAllTables().collect { liste ->
                _tables.value = liste
            }
        }
    }

    private fun chargerServeurs() {
        viewModelScope.launch {
            serveurRepository.getAllServeurs().collect { liste ->
                _serveurs.value = liste
            }
        }
    }

    // ==================== FILTRAGE ====================

    /**
     * Définit la date sélectionnée et recharge les affectations
     */
    fun setSelectedDate(date: String) {
        currentDate = date
        chargerAffectations()
    }

    /**
     * Définit le service sélectionné (DEJEUNER ou DINER) et recharge les affectations
     */
    fun setSelectedService(service: Service) {
        currentService = service
        chargerAffectations()
    }

    /**
     * Charge les affectations pour la date et le service sélectionnés
     */
    private fun chargerAffectations() {
        if (currentDate.isEmpty()) return

        viewModelScope.launch {
            affectationRepository.getAffectationsForService(currentDate, currentService)
                .collect { affectations ->
                    // Pour chaque affectation, on récupère les détails de la table et du serveur
                    val listeAvecDetails = affectations.map { affectation ->
                        val table = tableRepository.getTableById(affectation.tableId)
                        val serveur = serveurRepository.getServeurById(affectation.serveurId)
                        AffectationWithDetails(affectation, table, serveur)
                    }
                    _affectationsWithDetails.value = listeAvecDetails
                }
        }
    }

    // ==================== CRUD TABLES ====================

    /**
     * Ajoute une nouvelle table
     */
    fun ajouterTable(table: TableEntity) {
        viewModelScope.launch {
            tableRepository.insertTable(table)
        }
    }

    /**
     * Modifie une table existante
     */
    fun modifierTable(table: TableEntity) {
        viewModelScope.launch {
            tableRepository.updateTable(table)
        }
    }

    /**
     * Supprime une table
     */
    fun supprimerTable(table: TableEntity) {
        viewModelScope.launch {
            tableRepository.deleteTable(table)
        }
    }

    // ==================== CRUD AFFECTATIONS ====================

    /**
     * Crée une nouvelle affectation (table -> serveur pour un service)
     */
    fun ajouterAffectation(affectation: AffectationEntity) {
        viewModelScope.launch {
            affectationRepository.insertAffectation(affectation)
            chargerAffectations() // Rafraîchir la liste
        }
    }

    /**
     * Modifie une affectation existante
     */
    fun modifierAffectation(affectation: AffectationEntity) {
        viewModelScope.launch {
            affectationRepository.updateAffectation(affectation)
            chargerAffectations() // Rafraîchir la liste
        }
    }

    /**
     * Supprime une affectation
     */
    fun supprimerAffectation(affectation: AffectationEntity) {
        viewModelScope.launch {
            affectationRepository.deleteAffectation(affectation)
            chargerAffectations() // Rafraîchir la liste
        }
    }
}
