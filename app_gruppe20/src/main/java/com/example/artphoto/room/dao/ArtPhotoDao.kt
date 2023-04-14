package com.example.artphoto.room.dao

import androidx.room.*
import com.example.artphoto.images.model.*


@Dao
interface ArtPhotoDao {
    @Transaction
    @Query("SELECT * FROM cart_photo_table")
    suspend fun getAllCartPhotos(): MutableList<CartPhoto>?

    @Query("DELETE FROM size_table where size_id in (:ids)")
    suspend fun deleteAllSizes(ids: List<Int>)
    @Query("DELETE FROM frame_table where frame_id in (:ids)")
    suspend fun deleteAllFrames(ids: List<Int>)
    @Query("DELETE FROM art_photo_table where art_id in (:ids)")
    suspend fun deleteAllArtPhotos(ids: List<Int>)
    @Query("DELETE FROM cart_photo_table where cart_photo_id in (:ids)")
    suspend fun deleteAllCartPhotos(ids: List<Int>)

    @Transaction
    suspend fun deleteSelectedPhotos(ids: List<Int>) {
        deleteAllSizes(ids)
        deleteAllFrames(ids)
        deleteAllArtPhotos(ids)
        deleteAllCartPhotos(ids)
    }


//    @Delete
//    @Query("DELETE FROM cart_photo_table WHERE id = :cart_photo_id")
//    suspend fun deleteById(cartPhotoDB: CartPhotoDB)

    @Query("DELETE FROM size_table")
    suspend fun deleteAllSizes()
    @Query("DELETE FROM frame_table")
    suspend fun deleteAllFrames()
    @Query("DELETE FROM art_photo_table")
    suspend fun deleteAllArtPhotos()
    @Query("DELETE FROM cart_photo_table")
    suspend fun deleteAllCartPhotos()

    @Transaction
    suspend fun deleteAllTables() {
        deleteAllSizes()
        deleteAllFrames()
        deleteAllArtPhotos()
        deleteAllCartPhotos()
    }

    @Insert
    suspend fun insert(photoDB: CartPhotoDB)

    @Insert
    suspend fun insertArtPhoto(photo: ArtPhoto)

    @Insert
    suspend fun insertFrame(frame: Frame)

    @Insert
    suspend fun insertSize(size: Size)

}