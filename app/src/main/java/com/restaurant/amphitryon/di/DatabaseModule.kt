package com.restaurant.amphitryon.di

import android.content.Context
import androidx.room.Room
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
        ).build()
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
