package jan.dhan.darshak.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favoriteentity::class], version = 1)

    abstract class Favoritedatabase: RoomDatabase() {
        abstract fun favoritedao(): FavoriteDao
        companion object{
            @Volatile
            private var INSTANCE: Favoritedatabase?=null
            fun getInstance(context: Context): Favoritedatabase {
                synchronized(this){
                    var instance= INSTANCE
                    if(instance==null)
                    {
                        instance= Room.databaseBuilder(
                            context.applicationContext,
                            Favoritedatabase::class.java,
                            "favorite_places_database"
                        ).fallbackToDestructiveMigration()
                            .build()
                        INSTANCE =instance
                    }
                    return instance
                }
            }
        }
    }
