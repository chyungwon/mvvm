package com.example.photos.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.photos.viewmodels.RequestResult

fun LifecycleOwner.applyRequestResult(context: Context?, requestResult: LiveData<RequestResult>?, progressBar: View?) {
    context?.let {context ->
        requestResult?.observe(this) {
            progressBar?.visibility = if (!it.showProcess) View.GONE else View.VISIBLE
            if(it.networkError) {
                Toast.makeText(context.applicationContext, "네트웍 에러가 발생했습니다!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}