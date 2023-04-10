package com.example.artphoto.room.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.artphoto.images.model.CartPhotoDB
import com.example.artphoto.room.dao.ArtPhotoDao

@Database(entities = [CartPhotoDB::class], version = 1)
abstract class ArtPhotoDatabase: RoomDatabase() {
    abstract fun photoDao(): ArtPhotoDao

    companion object {

        private var INSTANCE: ArtPhotoDatabase? = null

        fun getDB(context: Context): ArtPhotoDatabase {
            if (INSTANCE == null) {
                synchronized(ArtPhotoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ArtPhotoDatabase::class.java,
                        "cart_photo_table"
                    ).build()
                }
            }
            return INSTANCE!!

        }


    }
}