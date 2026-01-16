package com.restaurant.amphitryon.data.local.database.dao

import androidx.room.*
import com.restaurant.amphitryon.data.local.database.entities.CommandeEntity
import com.restaurant.amphitryon.data.local.database.entities.LigneCommandeEntity
import com.restaurant.amphitryon.domain.model.EtatPlat
import com.restaurant.amphitryon.domain.model.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandeDao {
    
    // Commande operations
    @Query("SELECT * FROM commandes ORDER BY dateHeure DESC")
    fun getAllCommandes(): Flow<List<CommandeEntity>>
    
    @Query("SELECT * FROM commandes WHERE id = :commandeId")
    suspend fun getCommandeById(commandeId: Long): CommandeEntity?
    
    @Query("SELECT * FROM commandes WHERE tableId = :tableId ORDER BY dateHeure DESC")
    fun getCommandesByTable(tableId: Long): Flow<List<CommandeEntity>>
    
    @Query("SELECT * FROM commandes WHERE service = :service AND dateHeure LIKE :datePattern ORDER BY dateHeure DESC")
    fun getCommandesByService(service: Service, datePattern: String): Flow<List<CommandeEntity>>
    
    @Query("SELECT * FROM commandes WHERE estReglee = 0 ORDER BY dateHeure DESC")
    fun getUnpaidCommandes(): Flow<List<CommandeEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommande(commande: CommandeEntity): Long
    
    @Update
    suspend fun updateCommande(commande: CommandeEntity)
    
    @Delete
    suspend fun deleteCommande(commande: CommandeEntity)
    
    @Query("DELETE FROM commandes WHERE id = :commandeId")
    suspend fun deleteCommandeById(commandeId: Long)
    
    @Query("UPDATE commandes SET estReglee = 1 WHERE id = :commandeId")
    suspend fun markCommandeAsPaid(commandeId: Long)
    
    // LigneCommande operations
    @Query("SELECT * FROM lignes_commande WHERE commandeId = :commandeId")
    fun getLignesCommandeByCommande(commandeId: Long): Flow<List<LigneCommandeEntity>>
    
    @Query("SELECT * FROM lignes_commande WHERE id = :ligneId")
    suspend fun getLigneCommandeById(ligneId: Long): LigneCommandeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLigneCommande(ligneCommande: LigneCommandeEntity): Long
    
    @Update
    suspend fun updateLigneCommande(ligneCommande: LigneCommandeEntity)
    
    @Delete
    suspend fun deleteLigneCommande(ligneCommande: LigneCommandeEntity)
    
    @Query("DELETE FROM lignes_commande WHERE id = :ligneId")
    suspend fun deleteLigneCommandeById(ligneId: Long)
    
    @Query("UPDATE lignes_commande SET etat = :etat WHERE id = :ligneId")
    suspend fun updateLigneCommandeEtat(ligneId: Long, etat: EtatPlat)
}
