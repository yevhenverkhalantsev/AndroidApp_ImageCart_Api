package com.example.artphoto.room.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.artphoto.images.model.CartPhotoDB
import com.example.artphoto.room.dao.artPhotoDao

@Database(entities = [CartPhotoDB::class], version = 1)
abstract class artPhotoDatabase: RoomDatabase() {

    abstract fun photoDao(artPhotoDao: artPhotoDao)

    companion object {

        private var INSTANCE: artPhotoDatabase? = null

        fun getDB(context: Context): artPhotoDatabase {
            if (INSTANCE == null) {
                synchronized(artPhotoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        artPhotoDatabase::class.java,
                        "cart_photo_table"
                    ).build()
                }
            }
            return INSTANCE!!

        }


    }
}