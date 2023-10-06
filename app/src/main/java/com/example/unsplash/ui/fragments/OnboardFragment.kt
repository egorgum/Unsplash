package com.example.unsplash.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.unsplash.R
import com.example.unsplash.SharedPrefs.SharedPrefScreen
import com.example.unsplash.databinding.FragmentOnboardBinding
import com.example.unsplash.viewModels.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardFragment : Fragment() {

    private var binding: FragmentOnboardBinding? = null
    private val viewModel: OnboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!SharedPrefScreen(requireContext()).getFirst()){
            findNavController().navigate(R.id.authorizeFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPrefScreen(requireContext()).saveFirst(token = false)

        fun navNext(){
            when(viewModel.text.value){
                R.string.firs_recommendation -> {
                    viewModel.text.value = R.string.second_recommendation
                }
                R.string.second_recommendation -> {
                    viewModel.text.value = R.string.third_recommendation
                }
                R.string.third_recommendation -> {
                    findNavController().navigate(R.id.action_onboardFragment_to_authorizeFragment)
//                    val intent = Intent( requireContext(), MainActivity::class.java)
//                    startActivity(intent)
                }
            }
        }
        fun navBack(){
            when(viewModel.text.value){
                R.string.second_recommendation -> {
                    viewModel.text.value = R.string.firs_recommendation
                }
                R.string.third_recommendation -> {
                    viewModel.text.value = R.string.second_recommendation
                }
            }
        }
        binding!!.btNext.setOnClickListener {
            navNext()
        }
        binding!!.btBack.setOnClickListener {
            navBack()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.text.collect{
                binding!!.tvOnboard.text = getText(viewModel.text.value)
                when(it){
                    R.string.firs_recommendation -> {
                        binding!!.btBack.visibility = View.GONE
                    }
                    else -> binding!!.btBack.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}