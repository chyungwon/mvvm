package com.example.photos.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Query("select * from databasephoto LIMIT :limit OFFSET :page * :limit")
    suspend fun getList(page: Int, limit: Int): List<DatabasePhoto>

    @Query("SELECT * FROM databasephoto")
    fun getList(): DataSource.Factory<Int, DatabasePhoto>

    @Query("select COUNT(*) from databasephoto")
    suspend fun getTotalCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll( photos: List<DatabasePhoto>) : List<Long>

    @Query("UPDATE databasephoto SET like_yn=:likeYn WHERE id = :id")
    fun update(likeYn: String, id: String): Int
}