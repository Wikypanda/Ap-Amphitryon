package com.restaurant.amphitryon.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables")
data class TableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numero: Int,
    val nombrePlaces: Int,
    val estOccupee: Boolean = false
)
