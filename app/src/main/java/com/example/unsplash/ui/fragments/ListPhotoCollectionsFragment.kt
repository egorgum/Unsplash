package com.example.unsplash.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.unsplash.LoadAdapter
import com.example.unsplash.R
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.databinding.FragmentListPhotoCollectionsBinding
import com.example.unsplash.recycler.adapters.AdapterForSearching
import com.example.unsplash.viewModels.ListPhotoCollectionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ListPhotoCollectionsFragment : Fragment() {

    private var _binding: FragmentListPhotoCollectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListPhotoCollectionsViewModel by viewModels()
    private  val arg: ListPhotoCollectionsFragmentArgs by navArgs()
    private val myAdapter = AdapterForSearching({
            photo -> onItemClick(photo)},{isLiked, id -> onLikeClick(isLiked, id)})



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPhotoCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.adapter = myAdapter.withLoadStateFooter(LoadAdapter())
        binding.title.text = "${binding.title.text} ${arg.tittleCollection}"
        viewModel.paging(arg.id, SharedPrefToken(requireContext()).getText().toString())
        viewModel.pagingPhoto!!.onEach {
            myAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onItemClick(item: PhotosItem){
        try {
            val bundle = Bundle().apply {
                putString("photo_info", item.id)
            }
            findNavController().navigate(R.id.detailedFragment, bundle)
        }
        catch (e:Exception){
            Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLikeClick(liked: Boolean, id: String){
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.changeLike(liked,id,requireContext())
            }
            catch (e:Exception){
                Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        }
    }

}