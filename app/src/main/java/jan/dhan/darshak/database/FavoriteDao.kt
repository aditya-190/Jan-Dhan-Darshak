package jan.dhan.darshak.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun Insert(favoriteentity: Favoriteentity)

    @Query("Delete from 'Favorite_places_database' where id= :id")
    suspend fun Delete(id:String)

    @Query("Select * FROM 'Favorite_places_database'")
    fun fetchAllfavorites(): Flow<List<Favoriteentity>>
}