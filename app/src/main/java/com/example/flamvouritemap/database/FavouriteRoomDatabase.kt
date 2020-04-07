package com.example.flamvouritemap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flamvouritemap.model.FavouriteResult
import com.example.flamvouritemap.model.Result

@Database(entities = [FavouriteResult::class], version = 1, exportSchema = false)
abstract class FavouriteRoomDatabase : RoomDatabase() {
    abstract fun bookDao(): FavouriteDao

    companion object {
        private var INSTANCE: FavouriteRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FavouriteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(FavouriteRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                FavouriteRoomDatabase::class.java, "favourite_database"
                            )
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .addCallback(sRoomDatabaseCallback)
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback: Callback =
            object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }
            }
    }
}