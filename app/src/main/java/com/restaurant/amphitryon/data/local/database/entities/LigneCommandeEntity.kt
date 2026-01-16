package com.restaurant.amphitryon.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.restaurant.amphitryon.domain.model.EtatPlat

@Entity(
    tableName = "lignes_commande",
    foreignKeys = [
        ForeignKey(
            entity = CommandeEntity::class,
            parentColumns = ["id"],
            childColumns = ["commandeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlatEntity::class,
            parentColumns = ["id"],
            childColumns = ["platId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["commandeId"]), Index(value = ["platId"])]
)
data class LigneCommandeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val commandeId: Long,
    val platId: Long,
    val quantite: Int,
    val informationsComplementaires: String = "",
    val etat: EtatPlat = EtatPlat.COMMANDE
)
