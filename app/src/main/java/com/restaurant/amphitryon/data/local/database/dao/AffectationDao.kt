package com.restaurant.amphitryon.data.local.database.dao

import androidx.room.*
import com.restaurant.amphitryon.data.local.database.entities.AffectationEntity
import com.restaurant.amphitryon.domain.model.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface AffectationDao {
    
    @Query("SELECT * FROM affectations WHERE date = :date AND service = :service")
    fun getAffectationsForService(date: String, service: Service): Flow<List<AffectationEntity>>
    
    @Query("SELECT * FROM affectations WHERE serveurId = :serveurId AND date = :date AND service = :service")
    fun getAffectationsByServeur(serveurId: Long, date: String, service: Service): Flow<List<AffectationEntity>>
    
    @Query("SELECT * FROM affectations WHERE tableId = :tableId AND date = :date AND service = :service")
    suspend fun getAffectationByTable(tableId: Long, date: String, service: Service): AffectationEntity?
    
    @Query("SELECT * FROM affectations WHERE id = :affectationId")
    suspend fun getAffectationById(affectationId: Long): AffectationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAffectation(affectation: AffectationEntity): Long
    
    @Update
    suspend fun updateAffectation(affectation: AffectationEntity)
    
    @Delete
    suspend fun deleteAffectation(affectation: AffectationEntity)
    
    @Query("DELETE FROM affectations WHERE id = :affectationId")
    suspend fun deleteAffectationById(affectationId: Long)
    
    @Query("DELETE FROM affectations WHERE tableId = :tableId AND date = :date AND service = :service")
    suspend fun deleteAffectationByTable(tableId: Long, date: String, service: Service)
}
