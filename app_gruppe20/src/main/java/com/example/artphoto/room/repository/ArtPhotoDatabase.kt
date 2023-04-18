package com.example.artphoto.room.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.artphoto.images.model.*
import com.example.artphoto.room.dao.ArtPhotoDao

@Database(entities = [CartPhotoDB::class, Size::class, Frame::class, ArtPhoto::class], version = 1)
abstract class ArtPhotoDatabase: RoomDatabase() {
    abstract fun photoDao(): ArtPhotoDao
}