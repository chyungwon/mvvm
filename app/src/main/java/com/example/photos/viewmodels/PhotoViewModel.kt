package com.example.photos.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.photos.database.getDatabase
import com.example.photos.repository.PhotoRepository
import kotlinx.coroutines.launch
import java.io.IOException

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val photoRepository = PhotoRepository(getDatabase(application))

    val photoPagedList = photoRepository.pagedList

    val photoItem = photoRepository.item

    private val _currentId = MutableLiveData<String>()
    val currentId: LiveData<String>
        get() = _currentId
    private val _prevId = MutableLiveData<String>()
    val prevId: LiveData<String>
        get() = _prevId
    private val _nextId = MutableLiveData<String>()
    val nextId: LiveData<String>
        get() = _nextId

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

    fun selectCurrentId(id: String) {
        _currentId.value = id

        photoPagedList.value?.let {
            val currentIndex = it.indexOfFirst {
                return@indexOfFirst it?.id == id
            }

            if(currentIndex > 0) {
                _prevId.value = it[currentIndex-1]?.id
            }
            else {
                _prevId.value = null
            }
            if(currentIndex < it.size-1) {
                _nextId.value = it[currentIndex+1]?.id
            }
            else {
                _nextId.value = null
            }
        }
    }

}
