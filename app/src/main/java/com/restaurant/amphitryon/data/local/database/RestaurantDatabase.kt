package com.restaurant.amphitryon.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.restaurant.amphitryon.data.local.database.dao.*
import com.restaurant.amphitryon.data.local.database.entities.*

@Database(
    entities = [
        TableEntity::class,
        ServeurEntity::class,
        PlatEntity::class,
        PlatServiceEntity::class,
        AffectationEntity::class,
        CommandeEntity::class,
        LigneCommandeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun tableDao(): TableDao
    abstract fun serveurDao(): ServeurDao
    abstract fun platDao(): PlatDao
    abstract fun affectationDao(): AffectationDao
    abstract fun commandeDao(): CommandeDao
}
