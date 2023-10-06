package com.example.unsplash.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.unsplash.LoadAdapter
import com.example.unsplash.R
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.data.modelsCollections.CollectionPhoto
import com.example.unsplash.databinding.FragmentCollectionsBinding
import com.example.unsplash.recycler.adapters.AdapterCollections
import com.example.unsplash.viewModels.CollectionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CollectionsFragment: Fragment() {

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CollectionsViewModel by viewModels()
    private val myAdapter = AdapterCollections { collection -> onItemClick(collection) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.rvCollections.adapter = myAdapter.withLoadStateFooter(LoadAdapter())
            viewModel.paging(SharedPrefToken(requireContext()).getText().toString())
            viewModel.pagingCollections!!.onEach {
                myAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
        catch (e:Exception){
            binding.tvWrong.visibility = View.VISIBLE
            binding.rvCollections.visibility = View.GONE
            Log.d(TAG, "Ошибка коллекций: $e")
        }

    }
    private fun onItemClick(item: CollectionPhoto){
        try {
            val bundle = Bundle().apply {
                putString("id", item.id)
                putString("tittleCollection", item.title)
            }
            findNavController().navigate(R.id.listPhotoCollectionsFragment, bundle)
        }
        catch (e:Exception){
            Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}