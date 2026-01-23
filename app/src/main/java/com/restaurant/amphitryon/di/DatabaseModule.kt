package com.restaurant.amphitryon.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.restaurant.amphitryon.data.local.database.RestaurantDatabase
import com.restaurant.amphitryon.data.local.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideRestaurantDatabase(
        @ApplicationContext context: Context
    ): RestaurantDatabase {
        return Room.databaseBuilder(
            context,
            RestaurantDatabase::class.java,
            "restaurant_database"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Tables
                db.execSQL("INSERT INTO tables (numero, nombrePlaces, estOccupee) VALUES (1, 4, 0)")
                db.execSQL("INSERT INTO tables (numero, nombrePlaces, estOccupee) VALUES (2, 6, 0)")
                db.execSQL("INSERT INTO tables (numero, nombrePlaces, estOccupee) VALUES (3, 2, 0)")
                db.execSQL("INSERT INTO tables (numero, nombrePlaces, estOccupee) VALUES (4, 8, 0)")
                db.execSQL("INSERT INTO tables (numero, nombrePlaces, estOccupee) VALUES (5, 4, 0)")
                
                // Serveurs
                db.execSQL("INSERT INTO serveurs (nom, prenom) VALUES ('Dupont', 'Marie')")
                db.execSQL("INSERT INTO serveurs (nom, prenom) VALUES ('Martin', 'Pierre')")
                db.execSQL("INSERT INTO serveurs (nom, prenom) VALUES ('Bernard', 'Sophie')")
                
                // Plats
                db.execSQL("INSERT INTO plats (numero, nom, descriptif, categorie, prixBase) VALUES (1, 'Salade César', 'Salade romaine, parmesan, croûtons', 'ENTREE', 12.50)")
                db.execSQL("INSERT INTO plats (numero, nom, descriptif, categorie, prixBase) VALUES (2, 'Soupe à l''oignon', 'Soupe traditionnelle gratinée', 'ENTREE', 9.00)")
                db.execSQL("INSERT INTO plats (numero, nom, descriptif, categorie, prixBase) VALUES (3, 'Steak Frites', 'Entrecôte grillée avec frites maison', 'PLAT_PRINCIPAL', 24.00)")
                db.execSQL("INSERT INTO plats (numero, nom, descriptif, categorie, prixBase) VALUES (4, 'Saumon grillé', 'Saumon avec légumes de saison', 'PLAT_PRINCIPAL', 22.00)")
                db.execSQL("INSERT INTO plats (numero, nom, descriptif, categorie, prixBase) VALUES (5, 'Tarte Tatin', 'Tarte aux pommes caramélisées', 'DESSERT', 8.50)")
                db.execSQL("INSERT INTO plats (numero, nom, descriptif, categorie, prixBase) VALUES (6, 'Crème Brûlée', 'Crème vanille caramélisée', 'DESSERT', 7.50)")
            }
        })
        .build()
    }
    
    @Provides
    @Singleton
    fun provideTableDao(database: RestaurantDatabase): TableDao {
        return database.tableDao()
    }
    
    @Provides
    @Singleton
    fun provideServeurDao(database: RestaurantDatabase): ServeurDao {
        return database.serveurDao()
    }
    
    @Provides
    @Singleton
    fun providePlatDao(database: RestaurantDatabase): PlatDao {
        return database.platDao()
    }
    
    @Provides
    @Singleton
    fun provideAffectationDao(database: RestaurantDatabase): AffectationDao {
        return database.affectationDao()
    }
    
    @Provides
    @Singleton
    fun provideCommandeDao(database: RestaurantDatabase): CommandeDao {
        return database.commandeDao()
    }
}