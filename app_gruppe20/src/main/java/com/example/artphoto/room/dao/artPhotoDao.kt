package com.example.artphoto.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.artphoto.images.model.CartPhotoDB
import kotlinx.coroutines.selects.select


@Dao
interface artPhotoDao {

    @Query("SELECT * FROM cart_photo_table")
    suspend fun getAllPhotos(): List<CartPhotoDB>

    @Delete
    //@Query("DELETE FROM cart_photo_table WHERE id = :photoItemId")
    suspend fun deleteItemByID(cartPhotoDB: CartPhotoDB)

    @Query("DELETE FROM cart_photo_table")
    suspend fun deleteAll()
}