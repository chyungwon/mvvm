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
import com.example.photos.viewmodels.RequestResult
import kotlinx.coroutines.*
import java.lang.Exception


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

    private var _requestResult = MutableLiveData(RequestResult())
    val requestResult: LiveData<RequestResult>
        get() = _requestResult

    fun searchNextList() {
        CoroutineScope(Dispatchers.IO).launch {
            val totalCount = database.photoDao.getTotalCount()
            val nextPage = totalCount / PAGE_SIZE + 1
            searchList(nextPage, PAGE_SIZE)
        }
    }
    fun searchList(page: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _requestResult.postValue(_requestResult.value!!.apply {
                    showProcess = true
                })

                // 로컬디비에 데이터가 없으면 네트웍api에서 조회하고
                val photolist = PhotoNetwork.photoService.getList(page, limit)
                // 로컬디비에 동기화한다
                val insertedList = database.photoDao.insertAll(photolist.map {
                    it.asDatabaseModel()
                })
                println("insertedList(${insertedList.size}) : $insertedList")

                _requestResult.postValue(_requestResult.value!!.apply {
                    networkError = false
                })
            } catch(e: Exception) {
                e.printStackTrace()
                _requestResult.postValue(_requestResult.value!!.apply {
                    networkError = true
                })
            }
            _requestResult.postValue(_requestResult.value!!.apply {
                showProcess = false
            })

        }
    }

    suspend fun readItem(id: String) {
        // 네트웍api에서 상세정보를 조회한다
        withContext(Dispatchers.Default) {
            try {
                _requestResult.postValue(_requestResult.value!!.apply {
                    showProcess = true
                })

                val info = PhotoNetwork.photoService.getInfo(id)
                _item.postValue(info)

                _requestResult.postValue(_requestResult.value!!.apply {
                    networkError = false
                })
            } catch(e: Exception) {
                e.printStackTrace()
                _requestResult.postValue(_requestResult.value!!.apply {
                    networkError = true
                })
            }
            _requestResult.postValue(_requestResult.value!!.apply {
                showProcess = false
            })
        }
    }

    fun initItem() {
        _item.postValue(null)
    }

    suspend fun changeLikeYn(id: String, likeYn: String): Int {
        var updated = 0
        withContext(Dispatchers.IO) {
            _requestResult.postValue(_requestResult.value!!.apply {
                showProcess = true
            })

            updated = database.photoDao.update(likeYn, id)
            Log.d("repository", "updated : $updated")

            _requestResult.postValue(_requestResult.value!!.apply {
                showProcess = false
            })
        }

        return updated
    }

    companion object {
        val PAGE_SIZE = 5
    }
}
