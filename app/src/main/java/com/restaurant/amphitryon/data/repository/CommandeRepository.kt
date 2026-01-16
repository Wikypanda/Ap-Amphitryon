package com.restaurant.amphitryon.data.repository

import com.restaurant.amphitryon.data.local.database.dao.CommandeDao
import com.restaurant.amphitryon.data.local.database.entities.CommandeEntity
import com.restaurant.amphitryon.data.local.database.entities.LigneCommandeEntity
import com.restaurant.amphitryon.domain.model.EtatPlat
import com.restaurant.amphitryon.domain.model.Service
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommandeRepository @Inject constructor(
    private val commandeDao: CommandeDao
) {
    // Commande operations
    fun getAllCommandes(): Flow<List<CommandeEntity>> = commandeDao.getAllCommandes()
    
    suspend fun getCommandeById(commandeId: Long): CommandeEntity? = 
        commandeDao.getCommandeById(commandeId)
    
    fun getCommandesByTable(tableId: Long): Flow<List<CommandeEntity>> = 
        commandeDao.getCommandesByTable(tableId)
    
    fun getCommandesByService(service: Service, datePattern: String): Flow<List<CommandeEntity>> = 
        commandeDao.getCommandesByService(service, datePattern)
    
    fun getUnpaidCommandes(): Flow<List<CommandeEntity>> = commandeDao.getUnpaidCommandes()
    
    suspend fun insertCommande(commande: CommandeEntity): Long = 
        commandeDao.insertCommande(commande)
    
    suspend fun updateCommande(commande: CommandeEntity) = commandeDao.updateCommande(commande)
    
    suspend fun deleteCommande(commande: CommandeEntity) = commandeDao.deleteCommande(commande)
    
    suspend fun deleteCommandeById(commandeId: Long) = commandeDao.deleteCommandeById(commandeId)
    
    suspend fun markCommandeAsPaid(commandeId: Long) = commandeDao.markCommandeAsPaid(commandeId)
    
    // LigneCommande operations
    fun getLignesCommandeByCommande(commandeId: Long): Flow<List<LigneCommandeEntity>> = 
        commandeDao.getLignesCommandeByCommande(commandeId)
    
    suspend fun getLigneCommandeById(ligneId: Long): LigneCommandeEntity? = 
        commandeDao.getLigneCommandeById(ligneId)
    
    suspend fun insertLigneCommande(ligneCommande: LigneCommandeEntity): Long = 
        commandeDao.insertLigneCommande(ligneCommande)
    
    suspend fun updateLigneCommande(ligneCommande: LigneCommandeEntity) = 
        commandeDao.updateLigneCommande(ligneCommande)
    
    suspend fun deleteLigneCommande(ligneCommande: LigneCommandeEntity) = 
        commandeDao.deleteLigneCommande(ligneCommande)
    
    suspend fun deleteLigneCommandeById(ligneId: Long) = 
        commandeDao.deleteLigneCommandeById(ligneId)
    
    suspend fun updateLigneCommandeEtat(ligneId: Long, etat: EtatPlat) = 
        commandeDao.updateLigneCommandeEtat(ligneId, etat)
}
