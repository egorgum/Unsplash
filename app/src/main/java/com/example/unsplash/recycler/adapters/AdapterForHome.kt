package com.example.unsplash.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.databinding.OnePhotoBinding
import com.example.unsplash.room.entities.DataBasePhotosHome
import javax.inject.Inject

class AdapterForHome@Inject constructor(private val onClick:(DataBasePhotosHome) -> Unit, private val onClick1:(Boolean, String) -> Unit): PagingDataAdapter<DataBasePhotosHome, MyViewHolder>(
    DiffUtilCallback()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = OnePhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        var liked = item!!.photo_is_liked
        with(holder.binding){
            name.text = item.user_name
            tvLikes.text = item.likes_number.toString()
            item.let{
                Glide
                    .with(avatar.context)
                    .load(it.avatar)
                    .into(avatar)
                Glide
                    .with(photo.context)
                    .load(it.photo_uri)
                    .into(photo)
                if (it.photo_is_liked){
                    ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_red)
                }
                ivLike.setOnClickListener{
                    if(liked){
                        ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_black)
                        tvLikes.text = (tvLikes.text.toString().toInt() - 1).toString()
                        onClick1(liked, item.id_photo)
                        liked = false

                    }
                    else{
                        ivLike.setImageResource(R.drawable.ic_baseline_thumb_up_red)
                        tvLikes.text = (tvLikes.text.toString().toInt() + 1).toString()
                        onClick1(liked, item.id_photo)
                        liked = true
                    }

                }
            }
        }
        holder.binding.root.setOnClickListener{
            item.let {
                onClick(item)
            }
        }
    }
}

class MyViewHolder@Inject constructor(val binding: OnePhotoBinding): RecyclerView.ViewHolder(binding.root)

class  DiffUtilCallback: DiffUtil.ItemCallback<DataBasePhotosHome>(){
    override fun areContentsTheSame(oldItem: DataBasePhotosHome, newItem: DataBasePhotosHome): Boolean = oldItem.id_photo == newItem.id_photo
    override fun areItemsTheSame(oldItem: DataBasePhotosHome, newItem: DataBasePhotosHome): Boolean = oldItem == newItem
}