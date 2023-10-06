package com.example.unsplash.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.databinding.FragmentDetailedBinding
import com.example.unsplash.viewModels.DetailedPhotoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailedFragment : Fragment() {

    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailedPhotoViewModel by viewModels()
    private var isLiked: Boolean = false

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ map->
        Log.d("MyLog","$map")
        if (map.values.all { it }){
            Toast.makeText(context,"storage permissions granted", Toast.LENGTH_LONG).show()
            viewModel.download(requireContext())
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                viewModel.download(requireContext())
            }
            else{
                Toast.makeText(context,"storage permissions isn't granted", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            try{
                viewModel.getDetailedInfo(
                    token = SharedPrefToken(requireContext()).getText()!!,
                    id = arguments?.getString("photo_info")!!
                )
                Log.d(TAG,"Информация: ${viewModel.detailedInfo.value}")
                Glide
                    .with(binding.ivPhotoDetailed.context)
                    .load(viewModel.detailedInfo.value!!.urls.regular)
                    .into(binding.ivPhotoDetailed)
                Glide
                    .with(binding.avatar.context)
                    .load(viewModel.detailedInfo.value!!.user.profile_image.small)
                    .into(binding.avatar)
                if (viewModel.detailedInfo.value!!.liked_by_user) {
                    binding.ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_red)
                }
                binding.tvDownloads.text = viewModel.detailedInfo.value!!.downloads.toString()
                binding.tvLikes.text = viewModel.detailedInfo.value!!.likes.toString()
                binding.name.text = viewModel.detailedInfo.value!!.user.username
                if(viewModel.detailedInfo.value!!.exif.model != null) {
                    binding.tvExif.text =
                        "${getString(R.string.made_with)} ${viewModel.detailedInfo.value!!.exif.model}"
                }
                if(viewModel.detailedInfo.value!!.location?.city != null) {
                    binding.tvLocation.text =
                        "${getString(R.string.located_in)} ${viewModel.detailedInfo.value!!.location?.city}"
                }
                if (viewModel.detailedInfo.value!!.tags != null) {
                    val a: MutableList<String> = listOf("#").toMutableList()
                    viewModel.detailedInfo.value!!.tags?.forEach {
                        a.add(it.title.toString())
                    }
                    binding.tvTags.text = "${getString(R.string.tags)} ${
                        a.joinToString(separator = "#")
                    }"
                }
                isLiked = viewModel.detailedInfo.value!!.liked_by_user
            }
            catch (e:Exception){
                Log.d(TAG,"Ошибка детального экрана: $e")
                binding.avatar.visibility = View.GONE
                binding.tbShare.visibility = View.GONE
                binding.ivShare.visibility = View.GONE
                binding.name.visibility = View.GONE
                binding.tvLikes.visibility = View.GONE
                binding.tvTags.visibility = View.GONE
                binding.tvExif.visibility = View.GONE
                binding.tvLocation.visibility = View.GONE
                binding.tvDownloads.visibility = View.GONE
                binding.ivLike.visibility = View.GONE
                binding.ivDownload.visibility = View.GONE
                binding.ivPhotoDetailed.visibility = View.GONE

                binding.tvWrong.visibility = View.VISIBLE
            }
        }

        binding.ivShare.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val shareBody = "https://unsplash.com/photos/${arguments?.getString("photo_info")}"
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }

        binding.ivDownload.setOnClickListener {
            launcher.launch(REQUEST_PERMISSIONS)
        }

        binding.tvLocation.setOnClickListener {
            if (viewModel.detailedInfo.value?.location?.position?.latitude !=null ||
                viewModel.detailedInfo.value?.location?.position?.longitude !=null) {
                try {
                    val uri = "yandexmaps://maps.yandex.ru/?pt=${viewModel.detailedInfo.value!!.location?.position?.longitude}," +
                            "+${viewModel.detailedInfo.value!!.location?.position?.latitude}&z=12&l=map"
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(uri)
                    )
                    startActivity(intent)
                }
                catch (e:Exception){
                    Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivLike.setOnClickListener {
            if (isLiked){
                binding.ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_black)
                binding.tvLikes.text = (binding.tvLikes.text.toString().toInt() - 1).toString()
                clickLike(isLiked,viewModel.detailedInfo.value!!.id)
                isLiked = false
            }
            else{
                binding.ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_red)
                binding.tvLikes.text = (binding.tvLikes.text.toString().toInt() + 1).toString()
                clickLike(isLiked,viewModel.detailedInfo.value!!.id)
                isLiked = true
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.status.collect{

                    if (it == true){
                        viewModel.status.value = null
                        launch {
                            val snack = Snackbar.make(this@DetailedFragment.requireView(),
                                getString(R.string.photo_is_downloaded),
                                Snackbar.LENGTH_LONG).setAction(getString(R.string.gallery)) {
                                viewModel.openGallery(requireContext())
                            }
                            snack.show()
                        }
                    }
                }
            }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
    private fun clickLike(like:Boolean, id:String){
        viewLifecycleOwner.lifecycleScope.launch{
            try {
                viewModel.changeLike(like, id,requireContext())
            }
            catch (e:Exception){
                Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT<= Build.VERSION_CODES.P){
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

}