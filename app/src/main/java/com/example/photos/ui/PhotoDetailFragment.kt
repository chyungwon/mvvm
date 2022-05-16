package com.example.photos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photos.R
import com.example.photos.databinding.FragmentPhotoDetailBinding
import com.example.photos.databinding.FragmentPhotoListBinding
import com.example.photos.domain.PhotoItem
import com.example.photos.util.applyRequestResult
import com.example.photos.viewmodels.PhotoViewModel
import com.example.photos.viewmodels.ViewModelFactory

class PhotoDetailFragment  : Fragment() {

    private var binding: FragmentPhotoDetailBinding? = null

    private val photoViewModel: PhotoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(activity, ViewModelFactory(activity.application))
            .get(PhotoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoViewModel.run {
            currentId.observe(viewLifecycleOwner) {
                readItem(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_photo_detail,
            container,
            false)

        binding?.apply {
            setLifecycleOwner(viewLifecycleOwner)

            viewModel = photoViewModel

            photoCallback = PhotoDetailClickListener {
                when(it.id) {
                    R.id.btnPrev -> {
                        photoViewModel.selectCurrentId(photoViewModel.prevId.value!!)
                    }
                    R.id.btnNext -> {
                        photoViewModel.selectCurrentId(photoViewModel.nextId.value!!)
                    }
                }
            }

            applyRequestResult(context, photoViewModel.requestResult, progressBar)
        }

        return binding!!.root
    }
}

class PhotoDetailClickListener(val block: (View) -> Unit) {
    fun onClick(view: View) = block(view)
}