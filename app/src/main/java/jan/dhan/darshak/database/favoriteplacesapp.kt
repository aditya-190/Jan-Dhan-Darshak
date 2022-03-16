package jan.dhan.darshak.database

import android.app.Application

class favoriteplacesapp:Application() {
    val db by lazy{
        Favoritedatabase.getInstance(this)
    }
}