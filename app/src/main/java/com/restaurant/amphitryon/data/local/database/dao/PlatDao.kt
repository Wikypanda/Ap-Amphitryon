package com.restaurant.amphitryon.data.local.database.dao

import androidx.room.*
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.data.local.database.entities.PlatServiceEntity
import com.restaurant.amphitryon.domain.model.CategoriePlat
import com.restaurant.amphitryon.domain.model.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatDao {
    
    // Plat operations
    @Query("SELECT * FROM plats ORDER BY numero ASC")
    fun getAllPlats(): Flow<List<PlatEntity>>
    
    @Query("SELECT * FROM plats WHERE id = :platId")
    suspend fun getPlatById(platId: Long): PlatEntity?
    
    @Query("SELECT * FROM plats WHERE categorie = :categorie ORDER BY numero ASC")
    fun getPlatsByCategorie(categorie: CategoriePlat): Flow<List<PlatEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlat(plat: PlatEntity): Long
    
    @Update
    suspend fun updatePlat(plat: PlatEntity)
    
    @Delete
    suspend fun deletePlat(plat: PlatEntity)
    
    @Query("DELETE FROM plats WHERE id = :platId")
    suspend fun deletePlatById(platId: Long)
    
    // PlatService operations
    @Query("SELECT * FROM plat_services WHERE date = :date AND service = :service AND estPropose = 1")
    fun getPlatServicesForService(date: String, service: Service): Flow<List<PlatServiceEntity>>
    
    @Query("SELECT * FROM plat_services WHERE platId = :platId AND date = :date AND service = :service")
    suspend fun getPlatServiceByPlatAndService(platId: Long, date: String, service: Service): PlatServiceEntity?
    
    @Query("SELECT * FROM plat_services WHERE date = :date")
    fun getPlatServicesForDate(date: String): Flow<List<PlatServiceEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlatService(platService: PlatServiceEntity): Long
    
    @Update
    suspend fun updatePlatService(platService: PlatServiceEntity)
    
    @Delete
    suspend fun deletePlatService(platService: PlatServiceEntity)
    
    @Query("DELETE FROM plat_services WHERE id = :platServiceId")
    suspend fun deletePlatServiceById(platServiceId: Long)
    
    @Query("UPDATE plat_services SET estPropose = 0 WHERE platId = :platId AND date = :date AND service = :service")
    suspend fun removePlatFromService(platId: Long, date: String, service: Service)
    
    @Query("UPDATE plat_services SET quantiteDisponible = :quantity WHERE id = :platServiceId")
    suspend fun updateAvailableQuantity(platServiceId: Long, quantity: Int)
    
    @Query("UPDATE plat_services SET quantiteVendue = quantiteVendue + :quantity WHERE id = :platServiceId")
    suspend fun incrementSoldQuantity(platServiceId: Long, quantity: Int)
}
