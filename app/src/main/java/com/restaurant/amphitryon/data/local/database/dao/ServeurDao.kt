package com.restaurant.amphitryon.data.local.database.dao

import androidx.room.*
import com.restaurant.amphitryon.data.local.database.entities.ServeurEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServeurDao {
    
    @Query("SELECT * FROM serveurs ORDER BY nom, prenom ASC")
    fun getAllServeurs(): Flow<List<ServeurEntity>>
    
    @Query("SELECT * FROM serveurs WHERE id = :serveurId")
    suspend fun getServeurById(serveurId: Long): ServeurEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServeur(serveur: ServeurEntity): Long
    
    @Update
    suspend fun updateServeur(serveur: ServeurEntity)
    
    @Delete
    suspend fun deleteServeur(serveur: ServeurEntity)
    
    @Query("DELETE FROM serveurs WHERE id = :serveurId")
    suspend fun deleteServeurById(serveurId: Long)
}
