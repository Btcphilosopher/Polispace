package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val propertyId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_properties")
data class CustomPropertyEntity(
    @PrimaryKey val id: String,
    val titlePl: String,
    val titleEn: String,
    val descPl: String,
    val descEn: String,
    val addressPl: String,
    val addressEn: String,
    val city: String,
    val districtPl: String,
    val districtEn: String,
    val price: Double,
    val areaSqm: Double,
    val rooms: Int,
    val floor: Int,
    val maxFloors: Int,
    val buildYear: Int,
    val propertyType: String, // Enum name
    val marketType: String,   // Enum name
    val energyClass: String,  // Enum name
    val energyPerformance: Double,
    val isFurnished: Int, // 1 for true, 0 for false
    val agentName: String,
    val agentPhone: String,
    val agentEmail: String,
    val latitude: Double,
    val longitude: Double,
    val crimeRateScore: Double,
    val transportAccessScore: Double,
    val schoolRatingScore: Double,
    val infraZonePl: String,
    val infraZoneEn: String
)

@Dao
interface PolispaceDao {
    // Favorites
    @Query("SELECT propertyId FROM favorites")
    fun getFavoriteIdsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE propertyId = :propertyId")
    suspend fun deleteFavorite(propertyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE propertyId = :propertyId LIMIT 1)")
    suspend fun isFavorite(propertyId: String): Boolean

    // Custom Properties (Sellers)
    @Query("SELECT * FROM custom_properties ORDER BY id DESC")
    fun getCustomPropertiesFlow(): Flow<List<CustomPropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomProperty(property: CustomPropertyEntity)

    @Query("DELETE FROM custom_properties WHERE id = :id")
    suspend fun deleteCustomProperty(id: String)
}

@Database(
    entities = [FavoriteEntity::class, CustomPropertyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PolispaceDatabase : RoomDatabase() {
    abstract fun polispaceDao(): PolispaceDao
}
