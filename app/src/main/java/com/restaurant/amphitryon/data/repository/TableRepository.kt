package com.restaurant.amphitryon.data.repository

import com.restaurant.amphitryon.data.local.database.dao.TableDao
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TableRepository @Inject constructor(
    private val tableDao: TableDao
) {
    fun getAllTables(): Flow<List<TableEntity>> = tableDao.getAllTables()
    
    suspend fun getTableById(tableId: Long): TableEntity? = tableDao.getTableById(tableId)
    
    suspend fun getTableByNumero(numero: Int): TableEntity? = tableDao.getTableByNumero(numero)
    
    suspend fun insertTable(table: TableEntity): Long = tableDao.insertTable(table)
    
    suspend fun updateTable(table: TableEntity) = tableDao.updateTable(table)
    
    suspend fun deleteTable(table: TableEntity) = tableDao.deleteTable(table)
    
    suspend fun deleteTableById(tableId: Long) = tableDao.deleteTableById(tableId)
    
    suspend fun updateTableOccupancy(tableId: Long, occupied: Boolean) = 
        tableDao.updateTableOccupancy(tableId, occupied)
}
