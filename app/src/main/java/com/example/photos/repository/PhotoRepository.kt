package com.example.photos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList.BoundaryCallback
import com.example.photos.database.PhotoDatabase
import com.example.photos.database.asDomainModel
import com.example.photos.domain.PhotoItem
import com.example.photos.domain.asDatabaseModel
import com.example.photos.network.PhotoNetwork
import kotlinx.coroutines.*


class PhotoRepository(private val database: PhotoDatabase) {

    val pagedList  = LivePagedListBuilder(database.photoDao.getList().map {
            it.asDomainModel()
        }, PAGE_SIZE)
            .setBoundaryCallback(object : BoundaryCallback<PhotoItem>() {
                override fun onZeroItemsLoaded() {
                    Log.d("PaginationSource", "onZeroItemsLoaded")
                    super.onZeroItemsLoaded()

                    searchNextList()
                }

                override fun onItemAtFrontLoaded(itemAtFront: PhotoItem) {
                    Log.d("PaginationSource", "onItemAtFrontLoaded $itemAtFront")
                    super.onItemAtFrontLoaded(itemAtFront)
                }

                override fun onItemAtEndLoaded(itemAtEnd: PhotoItem) {
                    Log.d("PaginationSource", "onItemAtEndLoaded $itemAtEnd")
                    super.onItemAtEndLoaded(itemAtEnd)

                    searchNextList()
                }
            })
            .build()


    private val _item = MutableLiveData<PhotoItem>()
    val item: LiveData<PhotoItem>
        get() = _item

    fun searchNextList() {
        CoroutineScope(Dispatchers.IO).launch {
            val totalCount = database.photoDao.getTotalCount()
            val nextPage = totalCount / PAGE_SIZE + 1
            searchList(nextPage, PAGE_SIZE)
        }
    }
    fun searchList(page: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            // 로컬디비에 데이터가 없으면 네트웍api에서 조회하고
            val photolist = PhotoNetwork.photoService.getList(page, limit)
            // 로컬디비에 동기화한다
            val insertedList = database.photoDao.insertAll(photolist.map {
                it.asDatabaseModel()
            })
            println("insertedList(${insertedList.size}) : $insertedList")
        }
    }

    suspend fun readItem(id: String) {
        // 네트웍api에서 상세정보를 조회한다
        withContext(Dispatchers.Default) {
            val info = PhotoNetwork.photoService.getInfo(id)
            _item.postValue(info)
        }
    }

    fun initItem() {
        _item.postValue(null)
    }

    companion object {
        val PAGE_SIZE = 5
    }
}