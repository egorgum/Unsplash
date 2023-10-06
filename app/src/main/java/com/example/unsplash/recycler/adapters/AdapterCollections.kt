package com.example.unsplash.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplash.data.modelsCollections.CollectionPhoto
import com.example.unsplash.databinding.OneCollectionBinding
import javax.inject.Inject

class AdapterCollections @Inject constructor(private val onClick:(CollectionPhoto) -> Unit): PagingDataAdapter<CollectionPhoto, MyViewHolderCollection>(
    DiffUtilCallbackCollection()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCollection {
        val binding = OneCollectionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolderCollection(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolderCollection, position: Int) {
        val item = getItem(position)
        with(holder.binding){
            name.text = item?.user?.username
            tvNumber.text = "${item?.total_photos.toString()} " + tvNumber.text
            tvTitle.text = item?.title
            item.let{
                Glide
                    .with(avatar.context)
                    .load(it?.user?.profile_image?.small)
                    .into(avatar)
                Glide
                    .with(ivCollection.context)
                    .load(it?.cover_photo?.urls?.regular)
                    .into(ivCollection)
            }
        }
        holder.binding.root.setOnClickListener{
            item?.let {
                onClick(item)
            }
        }
    }
}

class MyViewHolderCollection@Inject constructor(val binding: OneCollectionBinding): RecyclerView.ViewHolder(binding.root)

class  DiffUtilCallbackCollection: DiffUtil.ItemCallback<CollectionPhoto>(){
    override fun areContentsTheSame(oldItem: CollectionPhoto, newItem: CollectionPhoto): Boolean = oldItem.id == newItem.id
    override fun areItemsTheSame(oldItem: CollectionPhoto, newItem: CollectionPhoto): Boolean = oldItem == newItem
}