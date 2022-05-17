package com.example.photos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photos.R
import com.example.photos.databinding.FragmentPhotoListBinding
import com.example.photos.util.applyRequestResult
import com.example.photos.viewmodels.PhotoViewModel
import com.example.photos.viewmodels.ViewModelFactory

class PhotoListFragment  : Fragment() {

    private var binding: FragmentPhotoListBinding? = null

    private var photoPagedListAdapter: PhotoPagedListAdapter? = null

    private val photoViewModel: PhotoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(activity, ViewModelFactory(activity.application))
            .get(PhotoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_photo_list,
            container,
            false)

        binding?.apply {
            setLifecycleOwner(viewLifecycleOwner)

            viewModel = photoViewModel

            photoPagedListAdapter = PhotoPagedListAdapter(PhotoListClickListener {
                photoViewModel.initItem()

                photoViewModel.selectCurrentItem(it)

                view?.findNavController()?.navigate(R.id.action_photoList_to_photoDetail)

            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = photoPagedListAdapter
            }

            applyRequestResult(context, photoViewModel.requestResult, progressBar)
        }

        return binding!!.root
    }
}