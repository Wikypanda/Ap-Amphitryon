package com.restaurant.amphitryon.data.local.database.dao

import androidx.room.*
import com.restaurant.amphitryon.data.local.database.entities.TableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {
    
    @Query("SELECT * FROM tables ORDER BY numero ASC")
    fun getAllTables(): Flow<List<TableEntity>>
    
    @Query("SELECT * FROM tables WHERE id = :tableId")
    suspend fun getTableById(tableId: Long): TableEntity?
    
    @Query("SELECT * FROM tables WHERE numero = :numero")
    suspend fun getTableByNumero(numero: Int): TableEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTable(table: TableEntity): Long
    
    @Update
    suspend fun updateTable(table: TableEntity)
    
    @Delete
    suspend fun deleteTable(table: TableEntity)
    
    @Query("DELETE FROM tables WHERE id = :tableId")
    suspend fun deleteTableById(tableId: Long)
    
    @Query("UPDATE tables SET estOccupee = :occupied WHERE id = :tableId")
    suspend fun updateTableOccupancy(tableId: Long, occupied: Boolean)
}
