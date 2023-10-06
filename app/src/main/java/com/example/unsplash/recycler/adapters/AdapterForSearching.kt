package com.example.unsplash.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.databinding.OnePhotoBinding
import javax.inject.Inject

class AdapterForSearching@Inject constructor(private val onClick:(PhotosItem) -> Unit, private val onClick1:(Boolean, String) -> Unit): PagingDataAdapter<PhotosItem, MyViewHolder>(
    DiffUtilCallback()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = OnePhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        var liked = item!!.liked_by_user
        with(holder.binding){
            name.text = item.user.username
            tvLikes.text = item.likes.toString()
            item.let{
                Glide
                    .with(avatar.context)
                    .load(it.user.profile_image.small)
                    .into(avatar)
                Glide
                    .with(photo.context)
                    .load(it.urls.regular)
                    .into(photo)
                if (it.liked_by_user){
                    ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_red)
                }
                ivLike.setOnClickListener{
                    if(liked){
                        ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_black)
                        tvLikes.text = (tvLikes.text.toString().toInt() - 1).toString()
                        onClick1(liked, item.id)
                        liked = false

                    }
                    else{
                        ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_red)
                        tvLikes.text = (tvLikes.text.toString().toInt() + 1).toString()
                        onClick1(liked, item.id)
                        liked = true
                    }

                }
            }
        }
        holder.binding.root.setOnClickListener{
            onClick(item)
        }
    }
}

class MyViewHolder@Inject constructor(val binding: OnePhotoBinding): RecyclerView.ViewHolder(binding.root)

class  DiffUtilCallback: DiffUtil.ItemCallback<PhotosItem>(){
    override fun areContentsTheSame(oldItem: PhotosItem, newItem: PhotosItem): Boolean = oldItem.id == newItem.id
    override fun areItemsTheSame(oldItem: PhotosItem, newItem: PhotosItem): Boolean = oldItem == newItem
}