package com.example.flamvouritemap.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flamvouritemap.model.FavouriteResult

@Dao
interface FavouriteDao {

    @get:Query("SELECT * FROM Favourite_Result")
    val favouritePlaces: MutableList<FavouriteResult>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAFavourite(favouriteResult: FavouriteResult)

    @Query("DELETE FROM Favourite_Result WHERE id = :id")
    fun deleteAFavourite(id:String)


}