package com.example.unsplash.ui.fragments

import android.os.Bundle
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
import com.example.unsplash.databinding.FragmentHomeBinding
import com.example.unsplash.recycler.AdapterForHome
import com.example.unsplash.room.entities.DataBasePhotosHome
import com.example.unsplash.states.HomeState
import com.example.unsplash.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val myAdapter = AdapterForHome({
            photo -> onItemClick(photo)},{isLiked, id -> onLikeClick(isLiked, id)})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.adapter = myAdapter.withLoadStateFooter(LoadAdapter())

        viewModel.paging(requireContext())
        viewModel.pagingPhoto?.onEach {
            myAdapter.submitData(it)
        }?.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.button.setOnClickListener {
            viewModel.paging(requireContext())
            viewModel.pagingPhoto?.onEach {
                myAdapter.submitData(it)
            }?.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        binding.btSearch.setOnClickListener {
            try {
                val a = binding.etSearch.text.toString()
                val bundle = Bundle().apply {
                    putString("query", a)
                }
                findNavController().navigate(R.id.searchingFragment, bundle)
            }
            catch (e:Exception){
                Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            HomeState.state.collect{
                when(it){
                    HomeState.SUCCESS-> binding.button.visibility = View.GONE
                    else -> {
                        Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                        binding.button.visibility = View.VISIBLE
                    }
                    }
                }
            }
        }


    private fun onItemClick(item: DataBasePhotosHome){
        try {
            val bundle = Bundle().apply {
                putString("photo_info", item.id_photo)
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDelete()
    }
}