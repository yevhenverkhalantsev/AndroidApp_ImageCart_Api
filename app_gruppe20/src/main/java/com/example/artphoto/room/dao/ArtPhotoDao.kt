package com.example.artphoto.room.dao

import androidx.room.*
import com.example.artphoto.images.model.*


@Dao
interface ArtPhotoDao {
    @Transaction
    @Query("SELECT * FROM cart_photo_table")
    suspend fun getAllCartPhotos(): MutableList<CartPhoto>


    //@Delete
    //@Query("DELETE FROM cart_photo_table WHERE id = :photoItemId")
    //suspend fun deleteItem(cartPhoto: CartPhoto)

    @Transaction
    @Query("DELETE FROM cart_photo_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(photoDB: CartPhotoDB)

    @Insert
    suspend fun insertArtPhoto(photo: ArtPhoto)

    @Insert
    suspend fun insertFrame(frame: Frame)

    @Insert
    suspend fun insertSize(size: Size)

}