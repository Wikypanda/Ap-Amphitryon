package com.restaurant.amphitryon.data.repository

import com.restaurant.amphitryon.data.local.database.dao.AffectationDao
import com.restaurant.amphitryon.data.local.database.entities.AffectationEntity
import com.restaurant.amphitryon.domain.model.Service
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AffectationRepository @Inject constructor(
    private val affectationDao: AffectationDao
) {
    fun getAffectationsForService(date: String, service: Service): Flow<List<AffectationEntity>> = 
        affectationDao.getAffectationsForService(date, service)
    
    fun getAffectationsByServeur(serveurId: Long, date: String, service: Service): Flow<List<AffectationEntity>> = 
        affectationDao.getAffectationsByServeur(serveurId, date, service)
    
    suspend fun getAffectationByTable(tableId: Long, date: String, service: Service): AffectationEntity? = 
        affectationDao.getAffectationByTable(tableId, date, service)
    
    suspend fun getAffectationById(affectationId: Long): AffectationEntity? = 
        affectationDao.getAffectationById(affectationId)
    
    suspend fun insertAffectation(affectation: AffectationEntity): Long = 
        affectationDao.insertAffectation(affectation)
    
    suspend fun updateAffectation(affectation: AffectationEntity) = 
        affectationDao.updateAffectation(affectation)
    
    suspend fun deleteAffectation(affectation: AffectationEntity) = 
        affectationDao.deleteAffectation(affectation)
    
    suspend fun deleteAffectationById(affectationId: Long) = 
        affectationDao.deleteAffectationById(affectationId)
    
    suspend fun deleteAffectationByTable(tableId: Long, date: String, service: Service) = 
        affectationDao.deleteAffectationByTable(tableId, date, service)
}
