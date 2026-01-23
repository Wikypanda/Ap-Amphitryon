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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefSalleViewModel @Inject constructor(
    private val tableRepository: TableRepository,
    private val affectationRepository: AffectationRepository,
    private val serveurRepository: ServeurRepository
) : ViewModel() {
    
    private val _tables = MutableStateFlow<List<TableEntity>>(emptyList())
    val tables: StateFlow<List<TableEntity>> = _tables.asStateFlow()
    
    private val _affectations = MutableStateFlow<List<AffectationEntity>>(emptyList())
    val affectations: StateFlow<List<AffectationEntity>> = _affectations.asStateFlow()
    
    private val _serveurs = MutableStateFlow<List<ServeurEntity>>(emptyList())
    val serveurs: StateFlow<List<ServeurEntity>> = _serveurs.asStateFlow()

    init {
        loadTables()
        loadServeurs()
    }
    
    private fun loadTables() {
        viewModelScope.launch {
            tableRepository.getAllTables().collect { tableList ->
                _tables.value = tableList
            }
        }
    }
    
    private fun loadServeurs() {
        viewModelScope.launch {
            serveurRepository.getAllServeurs().collect { serveurList ->
                _serveurs.value = serveurList
            }
        }
    }

    fun loadAffectationsForService(date: String, service: Service) {
        viewModelScope.launch {
            affectationRepository.getAffectationsForService(date, service).collect { affectationList ->
                _affectations.value = affectationList
            }
        }
    }
    
    fun insertTable(table: TableEntity) {
        viewModelScope.launch {
            tableRepository.insertTable(table)
        }
    }
    
    fun updateTable(table: TableEntity) {
        viewModelScope.launch {
            tableRepository.updateTable(table)
        }
    }
    
    fun deleteTable(table: TableEntity) {
        viewModelScope.launch {
            tableRepository.deleteTable(table)
        }
    }
    
    fun insertAffectation(affectation: AffectationEntity) {
        viewModelScope.launch {
            affectationRepository.insertAffectation(affectation)
        }
    }
    
    fun updateAffectation(affectation: AffectationEntity) {
        viewModelScope.launch {
            affectationRepository.updateAffectation(affectation)
        }
    }
    
    fun deleteAffectation(affectation: AffectationEntity) {
        viewModelScope.launch {
            affectationRepository.deleteAffectation(affectation)
        }
    }
}
