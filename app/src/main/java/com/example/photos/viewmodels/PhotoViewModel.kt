package com.example.photos.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.photos.database.getDatabase
import com.example.photos.domain.PhotoItem
import com.example.photos.repository.PhotoRepository
import kotlinx.coroutines.launch
import java.io.IOException

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val photoRepository = PhotoRepository(getDatabase(application))

    val photoPagedList = photoRepository.pagedList

    val photoItem = photoRepository.item

    val requestResult = photoRepository.requestResult

    private val _currentItem = MutableLiveData<PhotoItem>()
    val currentItem: LiveData<PhotoItem>
        get() = _currentItem
    private val _prevItem = MutableLiveData<PhotoItem>()
    val prevItem: LiveData<PhotoItem>
        get() = _prevItem
    private val _nextItem = MutableLiveData<PhotoItem>()
    val nextItem: LiveData<PhotoItem>
        get() = _nextItem

    fun readItem(id: String) {
        viewModelScope.launch {
            try {
                photoRepository.readItem(id)
            } catch (networkError: IOException) {
                networkError.printStackTrace()
            }
        }
    }

    fun initItem() {
        photoRepository.initItem()
    }

    fun selectCurrentItem(item: PhotoItem) {
        _currentItem.value = item

        photoPagedList.value?.let {
            val currentIndex = it.indexOfFirst {
                return@indexOfFirst it?.id == item.id
            }

            if(currentIndex > 0) {
                _prevItem.value = it[currentIndex-1]
            }
            else {
                _prevItem.value = null
            }
            if(currentIndex < it.size-1) {
                _nextItem.value = it[currentIndex+1]
            }
            else {
                _nextItem.value = null
            }
        }

        readItem(item.id)
    }

    fun changeLikeYn() {
        viewModelScope.launch {
            currentItem.value?.run {
                val new_yn = if(like_yn == "Y"){"N"}else{"Y"}
                val updated = photoRepository.changeLikeYn(id, new_yn)
                if(updated > 0) {
                    _currentItem.postValue(_currentItem.value!!.apply { like_yn = new_yn })
                }
            }
        }
    }

}
