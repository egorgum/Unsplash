package com.example.unsplash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.databinding.LoadStateBinding

class LoadAdapter: LoadStateAdapter<LoadViewHolder>() {
    override fun onBindViewHolder(holder: LoadViewHolder, loadState: LoadState) = Unit

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadViewHolder {
        val binding = LoadStateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadViewHolder(binding)
    }

}
class LoadViewHolder(binding: LoadStateBinding) : RecyclerView.ViewHolder(binding.root)