package com.restaurant.amphitryon.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.restaurant.amphitryon.domain.model.Service

@Entity(
    tableName = "plat_services",
    foreignKeys = [
        ForeignKey(
            entity = PlatEntity::class,
            parentColumns = ["id"],
            childColumns = ["platId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["platId"])]
)
data class PlatServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val platId: Long,
    val date: String, // Format: YYYY-MM-DD
    val service: Service,
    val prixVente: Double,
    val quantiteDisponible: Int,
    val quantiteVendue: Int = 0,
    val estPropose: Boolean = true
)
