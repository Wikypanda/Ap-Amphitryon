package com.restaurant.amphitryon.presentation.serveur

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.amphitryon.data.local.database.entities.CommandeEntity
import com.restaurant.amphitryon.data.local.database.entities.LigneCommandeEntity
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import com.restaurant.amphitryon.data.repository.CommandeRepository
import com.restaurant.amphitryon.data.repository.PlatRepository
import com.restaurant.amphitryon.data.repository.TableRepository
import com.restaurant.amphitryon.domain.model.EtatPlat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServeurViewModel @Inject constructor(
    private val commandeRepository: CommandeRepository,
    private val tableRepository: TableRepository,
    private val platRepository: PlatRepository
) : ViewModel() {
    
    private val _commandes = MutableStateFlow<List<CommandeEntity>>(emptyList())
    val commandes: StateFlow<List<CommandeEntity>> = _commandes.asStateFlow()
    
    private val _lignesCommande = MutableStateFlow<List<LigneCommandeEntity>>(emptyList())
    val lignesCommande: StateFlow<List<LigneCommandeEntity>> = _lignesCommande.asStateFlow()
    
    private val _tables = MutableStateFlow<List<TableEntity>>(emptyList())
    val tables: StateFlow<List<TableEntity>> = _tables.asStateFlow()

    private val _plats = MutableStateFlow<List<PlatEntity>>(emptyList())
    val plats: StateFlow<List<PlatEntity>> = _plats.asStateFlow()

    init {
        loadCommandes()
        loadTables()
        loadPlats()
    }
    
    private fun loadCommandes() {
        viewModelScope.launch {
            commandeRepository.getAllCommandes().collect { commandeList ->
                _commandes.value = commandeList
            }
        }
    }
    
    private fun loadTables() {
        viewModelScope.launch {
            tableRepository.getAllTables().collect { tableList ->
                _tables.value = tableList
            }
        }
    }

    private fun loadPlats() {
        viewModelScope.launch {
            platRepository.getAllPlats().collect { platList ->
                _plats.value = platList
            }
        }
    }

    fun loadLignesCommande(commandeId: Long) {
        viewModelScope.launch {
            commandeRepository.getLignesCommandeByCommande(commandeId).collect { ligneList ->
                _lignesCommande.value = ligneList
            }
        }
    }
    
    suspend fun getLignesCommandeForCommande(commandeId: Long): List<LigneCommandeEntity> {
        return commandeRepository.getLignesCommandeByCommande(commandeId).first()
    }

    fun insertCommande(commande: CommandeEntity) {
        viewModelScope.launch {
            commandeRepository.insertCommande(commande)
        }
    }
    
    fun updateCommande(commande: CommandeEntity) {
        viewModelScope.launch {
            commandeRepository.updateCommande(commande)
        }
    }
    
    fun deleteCommande(commande: CommandeEntity) {
        viewModelScope.launch {
            commandeRepository.deleteCommande(commande)
        }
    }
    
    fun markCommandeAsPaid(commandeId: Long) {
        viewModelScope.launch {
            commandeRepository.markCommandeAsPaid(commandeId)
        }
    }
    
    fun insertLigneCommande(ligneCommande: LigneCommandeEntity) {
        viewModelScope.launch {
            commandeRepository.insertLigneCommande(ligneCommande)
        }
    }
    
    fun updateLigneCommande(ligneCommande: LigneCommandeEntity) {
        viewModelScope.launch {
            commandeRepository.updateLigneCommande(ligneCommande)
        }
    }
    
    fun updateLigneCommandeEtat(ligneId: Long, etat: EtatPlat) {
        viewModelScope.launch {
            commandeRepository.updateLigneCommandeEtat(ligneId, etat)
        }
    }
    
    fun deleteLigneCommande(ligneCommande: LigneCommandeEntity) {
        viewModelScope.launch {
            commandeRepository.deleteLigneCommande(ligneCommande)
        }
    }
}
