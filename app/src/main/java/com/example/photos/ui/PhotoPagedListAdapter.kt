package com.example.photos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.photos.databinding.PhotoListItemBinding
import com.example.photos.domain.PhotoItem

class PhotoPagedListAdapter(private val callback: PhotoListClickListener) : PagedListAdapter<PhotoItem, PhotoListViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val photoItem: PhotoItem? = getItem(position)

            it.photo = photoItem
            it.photoCallback = callback
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val withDataBinding: PhotoListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PhotoListViewHolder.LAYOUT,
            parent,
            false)
        return PhotoListViewHolder(withDataBinding)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<PhotoItem>() {
            override fun areItemsTheSame(oldConcert: PhotoItem,
                                         newConcert: PhotoItem) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: PhotoItem,
                                            newConcert: PhotoItem) = oldConcert == newConcert
        }
    }
}