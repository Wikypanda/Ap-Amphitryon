package com.restaurant.amphitryon.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.restaurant.amphitryon.domain.model.Service

@Entity(
    tableName = "commandes",
    foreignKeys = [
        ForeignKey(
            entity = TableEntity::class,
            parentColumns = ["id"],
            childColumns = ["tableId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tableId"])]
)
data class CommandeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tableId: Long,
    val dateHeure: String, // Format: YYYY-MM-DD HH:MM:SS
    val service: Service,
    val estReglee: Boolean = false
)
