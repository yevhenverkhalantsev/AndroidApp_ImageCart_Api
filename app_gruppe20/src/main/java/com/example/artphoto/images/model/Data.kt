package com.example.artphoto.images.model

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "frame_table")
data class Frame(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "frame_id") val id: Int = 0,
    val name: String,
    val price: Int
)

@Entity(tableName = "size_table")
data class Size(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "size_id") val id: Int = 0,
    val name: String,
    val dimensions: String,
    val price: Int
)

data class CartPhoto(
    @Embedded val cartPhotoDB: CartPhotoDB,
    @Relation(
        parentColumn = "cart_photo_id",
        entityColumn = "art_id",
        entity = ArtPhoto::class
    ) val photo: ArtPhoto,
    @Relation(
        parentColumn = "cart_photo_id",
        entityColumn = "frame_id",
        entity = Frame::class
    ) val frame: Frame,
    @Relation(
        parentColumn = "cart_photo_id",
        entityColumn = "size_id",
        entity = Size::class
    ) val size: Size,
)

@Entity(tableName = "cart_photo_table")
data class CartPhotoDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "cart_photo_id") val id: Int = 0,
    val price: Int,
    val artistName: String,
    val amount: Int
)



@JsonClass(generateAdapter = true)
@Entity(tableName = "art_photo_table")
data class ArtPhoto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "art_id") val db_id: Int = 0,
    @Json(name = "id") val id: Int = 1,
    @Json(name = "albumId") @ColumnInfo(name = "album_id") val albumId: Int=-1,
    @Json(name = "title") val title: String="undefined",
    @Json(name = "url") val url: String="undefined",
    @Json(name = "thumbnailUrl") @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String="undefined",
)

@JsonClass(generateAdapter = true)
data class ArtArtist(
    @Json(name = "id") val id: Int=-1,
    @Json(name = "name") val name: String="undefined",
    @Json(name = "email") val email: String="undefined",
    @Json(name = "phone") val phone: String="undefined",
    @Json(name = "website") val website: String="undefined",
)

@JsonClass(generateAdapter = true)
data class ArtAlbum(
    @Json(name = "userId") val userId: Int = -1,
    @Json(name = "id") val id: Int = -1,
    @Json(name = "title") val title: String = "undefined",
)