package com.example.photos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.photos.R
import com.example.photos.databinding.FragmentPhotoDetailBinding
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
            readItem(currentItem.value!!.id)
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
                        photoViewModel.selectCurrentItem(photoViewModel.prevItem.value!!)
                    }
                    R.id.btnNext -> {
                        photoViewModel.selectCurrentItem(photoViewModel.nextItem.value!!)
                    }
                    R.id.btnHeart -> {
                        photoViewModel.changeLikeYn()
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