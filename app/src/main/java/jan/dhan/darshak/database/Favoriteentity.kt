package jan.dhan.darshak.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite_places_database")
data class Favoriteentity(
    @PrimaryKey
    val id: String,
    val name: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val rating:String?,
    val ratingCount: String?,
    val open:String,
    val close: String?,
    val timings:String,
    val phoneNumber:String?,
    val website:String?
)
