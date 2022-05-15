package com.example.photos.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photos.domain.PhotoItem

@Entity
data class DatabasePhoto constructor(
    @PrimaryKey
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String)

fun DatabasePhoto.asDomainModel(): PhotoItem {
    return PhotoItem(
        id = id,
        author = author,
        width = width,
        height = height,
        url = url,
        download_url = download_url
    )
}
