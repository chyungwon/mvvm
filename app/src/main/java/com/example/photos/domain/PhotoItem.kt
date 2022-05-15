package com.example.photos.domain

import com.example.photos.database.DatabasePhoto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoItem(val id: String,
                     val author: String,
                     val width: Int,
                     val height: Int,
                     val url: String,
                     val download_url: String)

fun PhotoItem.getSize(): String {
    return "$width X $height"
}


fun PhotoItem.asDatabaseModel(): DatabasePhoto {
    return DatabasePhoto(
        id = id,
        author = author,
        width = width,
        height = height,
        url = url,
        download_url = download_url
    )
}
