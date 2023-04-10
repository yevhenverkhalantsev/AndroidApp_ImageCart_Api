package com.example.artphoto.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.artphoto.images.model.CartPhotoDB


@Dao
interface ArtPhotoDao {

    @Query("SELECT * FROM cart_photo_table")
    suspend fun getAllPhotos(): MutableList<CartPhotoDB>

    @Delete
    //@Query("DELETE FROM cart_photo_table WHERE id = :photoItemId")
    suspend fun deleteItem(cartPhotoDB: CartPhotoDB)

    @Query("DELETE FROM cart_photo_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(photoDB: CartPhotoDB)

}