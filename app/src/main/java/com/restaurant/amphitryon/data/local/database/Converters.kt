package com.restaurant.amphitryon.data.local.database

import androidx.room.TypeConverter
import com.restaurant.amphitryon.domain.model.CategoriePlat
import com.restaurant.amphitryon.domain.model.EtatPlat
import com.restaurant.amphitryon.domain.model.Service

class Converters {
    
    @TypeConverter
    fun fromService(value: Service): String {
        return value.name
    }
    
    @TypeConverter
    fun toService(value: String): Service {
        return Service.valueOf(value)
    }
    
    @TypeConverter
    fun fromCategoriePlat(value: CategoriePlat): String {
        return value.name
    }
    
    @TypeConverter
    fun toCategoriePlat(value: String): CategoriePlat {
        return CategoriePlat.valueOf(value)
    }
    
    @TypeConverter
    fun fromEtatPlat(value: EtatPlat): String {
        return value.name
    }
    
    @TypeConverter
    fun toEtatPlat(value: String): EtatPlat {
        return EtatPlat.valueOf(value)
    }
}
