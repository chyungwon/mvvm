/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.photos.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photos.domain.PhotoItem
import com.example.photos.ui.PhotoListAdapter
import com.example.photos.ui.PhotoPagedListAdapter


@BindingAdapter("photoWidth", "photoHeight")
fun showSize(textView: TextView, photoWidth: Int, photoHeight: Int) {
    textView.text = "$photoWidth X $photoHeight"
}

@BindingAdapter("showProgress")
fun hideIfLoaded(view: View, showProgress: Boolean?) {
    view.visibility = if (showProgress != true) View.GONE else View.VISIBLE
}

@BindingAdapter("list")
fun showList(recyclerView: RecyclerView, list: LiveData<PagedList<PhotoItem>>) {
    list.value?.let {
        val photoListAdapter = recyclerView.adapter as PhotoPagedListAdapter
        photoListAdapter.submitList(it)
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(imageView.context).load(it).into(imageView)
    }
}

@BindingAdapter("photoId")
fun hideIfNoneId(view: View, photoId: String?) {
    view.visibility = if (photoId != null) View.VISIBLE else View.INVISIBLE
}