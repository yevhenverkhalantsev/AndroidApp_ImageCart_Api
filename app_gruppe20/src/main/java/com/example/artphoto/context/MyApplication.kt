package com.example.artphoto.context

import android.app.Application
import com.example.artphoto.context.injection.AppModule
import com.example.artphoto.home.view.HomeFragment
import com.example.artphoto.images.view.ImagesFragment
import com.example.artphoto.images.viewmodel.repository.injection.ApiModule
import com.example.artphoto.room.injection.DatabaseModule
import com.example.artphoto.selected.view.SelectedImageFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, AppModule::class, DatabaseModule::class])
interface AppComponent {
    fun inject(imagesFragment: ImagesFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(selectedImageFragment: SelectedImageFragment)
}

class MyApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


}