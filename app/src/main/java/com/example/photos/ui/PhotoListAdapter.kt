package com.example.photos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.photos.R
import com.example.photos.databinding.PhotoListItemBinding
import com.example.photos.domain.PhotoItem

class PhotoListAdapter(val callback: PhotoListClickListener) : RecyclerView.Adapter<PhotoListViewHolder>() {

    var photos: List<PhotoItem> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val withDataBinding: PhotoListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PhotoListViewHolder.LAYOUT,
            parent,
            false)
        return PhotoListViewHolder(withDataBinding)
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.photo = photos[position]
            it.photoCallback = callback
        }
    }

}

class PhotoListViewHolder(val viewDataBinding: PhotoListItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.photo_list_item
    }
}

class PhotoListClickListener(val block: (PhotoItem) -> Unit) {
    fun onClick(photoItem: PhotoItem) = block(photoItem)
}