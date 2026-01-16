package com.restaurant.amphitryon.data.repository

import com.restaurant.amphitryon.data.local.database.dao.ServeurDao
import com.restaurant.amphitryon.data.local.database.entities.ServeurEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServeurRepository @Inject constructor(
    private val serveurDao: ServeurDao
) {
    fun getAllServeurs(): Flow<List<ServeurEntity>> = serveurDao.getAllServeurs()
    
    suspend fun getServeurById(serveurId: Long): ServeurEntity? = serveurDao.getServeurById(serveurId)
    
    suspend fun insertServeur(serveur: ServeurEntity): Long = serveurDao.insertServeur(serveur)
    
    suspend fun updateServeur(serveur: ServeurEntity) = serveurDao.updateServeur(serveur)
    
    suspend fun deleteServeur(serveur: ServeurEntity) = serveurDao.deleteServeur(serveur)
    
    suspend fun deleteServeurById(serveurId: Long) = serveurDao.deleteServeurById(serveurId)
}
