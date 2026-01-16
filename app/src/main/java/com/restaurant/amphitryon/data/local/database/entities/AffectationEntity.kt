package com.restaurant.amphitryon.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.restaurant.amphitryon.domain.model.Service

@Entity(
    tableName = "affectations",
    foreignKeys = [
        ForeignKey(
            entity = TableEntity::class,
            parentColumns = ["id"],
            childColumns = ["tableId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ServeurEntity::class,
            parentColumns = ["id"],
            childColumns = ["serveurId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tableId"]), Index(value = ["serveurId"])]
)
data class AffectationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tableId: Long,
    val serveurId: Long,
    val date: String, // Format: YYYY-MM-DD
    val service: Service
)
