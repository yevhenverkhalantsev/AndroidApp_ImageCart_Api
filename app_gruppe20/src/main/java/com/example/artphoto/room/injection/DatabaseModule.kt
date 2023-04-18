package com.example.artphoto.room.injection

import android.app.Application
import androidx.room.Room
import com.example.artphoto.room.dao.ArtPhotoDao
import com.example.artphoto.room.repository.ArtPhotoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(mApplication: Application): ArtPhotoDatabase {
        synchronized(ArtPhotoDatabase::class) {
            return Room.databaseBuilder(
                mApplication.applicationContext,
                ArtPhotoDatabase::class.java,
                "photo_database"
            ).build()
        }
    }

    @Provides
    @Singleton
    fun provideArtPhotoDao(artPhotoDatabase: ArtPhotoDatabase): ArtPhotoDao = artPhotoDatabase.photoDao()

}