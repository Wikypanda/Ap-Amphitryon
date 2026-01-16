package com.restaurant.amphitryon.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.restaurant.amphitryon.domain.model.CategoriePlat

@Entity(tableName = "plats")
data class PlatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numero: Int,
    val nom: String,
    val descriptif: String,
    val categorie: CategoriePlat,
    val prixBase: Double
)
