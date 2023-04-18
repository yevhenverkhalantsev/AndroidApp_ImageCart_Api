package com.example.artphoto.context.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val mApplication: Application){
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return mApplication
    }
}