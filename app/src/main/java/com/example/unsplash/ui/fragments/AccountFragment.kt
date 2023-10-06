package com.example.unsplash.ui.fragments

import android.app.AlertDialog
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
import com.bumptech.glide.Glide
import com.example.unsplash.LoadAdapter
import com.example.unsplash.R
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.databinding.FragmentAccountBinding
import com.example.unsplash.recycler.adapters.AdapterForSearching
import com.example.unsplash.viewModels.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var binding: FragmentAccountBinding? = null
    private val viewModel: AccountViewModel by viewModels()

    private val myAdapter = AdapterForSearching({
            photo -> onItemClick(photo)},{isLiked, id -> onLikeClick(isLiked, id)})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            try {
                val token = SharedPrefToken(requireContext()).getText()!!
                viewModel.getMe(token)
                val username = viewModel.me.value!!.username
                viewModel.getPublic(token = token, username = username!!)
                Glide
                    .with(binding!!.avatar.context)
                    .load(viewModel.mePublic.value?.profile_image?.medium)
                    .into(binding!!.avatar)
                binding!!.tvName.text = viewModel.me.value!!.first_name
                binding!!.tvLastName.text = viewModel.me.value!!.last_name
                binding!!.tvEmail.text = viewModel.me.value!!.email
                binding!!.tvLocation.text = "${getString(R.string.location)} ${viewModel.me.value?.location}"

                binding!!.avatar.visibility = View.VISIBLE
                binding!!.tvName.visibility = View.VISIBLE
                binding!!.tvLastName.visibility = View.VISIBLE
                binding!!.tvEmail.visibility = View.VISIBLE
                binding!!.tvLocation.visibility = View.VISIBLE
                binding!!.toolbar.visibility = View.VISIBLE
                binding!!.tvWrong.visibility = View.GONE

                binding!!.rv.adapter = myAdapter.withLoadStateFooter(LoadAdapter())
                viewModel.paging(username, SharedPrefToken(requireContext()).getText().toString())
                viewModel.pagingPhoto!!.onEach {
                    myAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
            catch (e: Exception) {
                Log.d(TAG,"Ошибка профиля: $e")
                binding!!.avatar.visibility = View.GONE
                binding!!.tvName.visibility = View.GONE
                binding!!.tvLastName.visibility = View.GONE
                binding!!.tvEmail.visibility = View.GONE
                binding!!.tvLocation.visibility = View.GONE
                binding!!.toolbar.visibility = View.GONE
                binding!!.tvWrong.visibility = View.VISIBLE
            }
        }
        binding!!.btExit.setOnClickListener {
            AlertDialog
                .Builder(requireContext())
                .setTitle(R.string.exit)
                .setMessage(R.string.message)
                .setPositiveButton(R.string.yes){_, _ -> onClickYes()}
                .setNegativeButton(R.string.no){dialog,_ -> dialog.cancel()}.show()
        }
    }
    private fun onClickYes(){
        SharedPrefToken(requireContext()).clearText()
        findNavController().navigate(R.id.action_navigation_account_to_authorizeFragment)
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


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}