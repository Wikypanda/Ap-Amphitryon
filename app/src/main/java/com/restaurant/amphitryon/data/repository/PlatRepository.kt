package com.restaurant.amphitryon.data.repository

import com.restaurant.amphitryon.data.local.database.dao.PlatDao
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.data.local.database.entities.PlatServiceEntity
import com.restaurant.amphitryon.domain.model.CategoriePlat
import com.restaurant.amphitryon.domain.model.Service
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlatRepository @Inject constructor(
    private val platDao: PlatDao
) {
    // Plat operations
    fun getAllPlats(): Flow<List<PlatEntity>> = platDao.getAllPlats()
    
    suspend fun getPlatById(platId: Long): PlatEntity? = platDao.getPlatById(platId)
    
    fun getPlatsByCategorie(categorie: CategoriePlat): Flow<List<PlatEntity>> = 
        platDao.getPlatsByCategorie(categorie)
    
    suspend fun insertPlat(plat: PlatEntity): Long = platDao.insertPlat(plat)
    
    suspend fun updatePlat(plat: PlatEntity) = platDao.updatePlat(plat)
    
    suspend fun deletePlat(plat: PlatEntity) = platDao.deletePlat(plat)
    
    suspend fun deletePlatById(platId: Long) = platDao.deletePlatById(platId)
    
    // PlatService operations
    fun getPlatServicesForService(date: String, service: Service): Flow<List<PlatServiceEntity>> = 
        platDao.getPlatServicesForService(date, service)
    
    suspend fun getPlatServiceByPlatAndService(platId: Long, date: String, service: Service): PlatServiceEntity? = 
        platDao.getPlatServiceByPlatAndService(platId, date, service)
    
    fun getPlatServicesForDate(date: String): Flow<List<PlatServiceEntity>> = 
        platDao.getPlatServicesForDate(date)
    
    suspend fun insertPlatService(platService: PlatServiceEntity): Long = 
        platDao.insertPlatService(platService)
    
    suspend fun updatePlatService(platService: PlatServiceEntity) = 
        platDao.updatePlatService(platService)
    
    suspend fun deletePlatService(platService: PlatServiceEntity) = 
        platDao.deletePlatService(platService)
    
    suspend fun deletePlatServiceById(platServiceId: Long) = 
        platDao.deletePlatServiceById(platServiceId)
    
    suspend fun removePlatFromService(platId: Long, date: String, service: Service) = 
        platDao.removePlatFromService(platId, date, service)
    
    suspend fun updateAvailableQuantity(platServiceId: Long, quantity: Int) = 
        platDao.updateAvailableQuantity(platServiceId, quantity)
    
    suspend fun incrementSoldQuantity(platServiceId: Long, quantity: Int) = 
        platDao.incrementSoldQuantity(platServiceId, quantity)
}
