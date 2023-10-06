package com.example.unsplash.ui.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.unsplash.R
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.databinding.FragmentAuthorizeBinding
import com.example.unsplash.states.AuthenticationState
import com.example.unsplash.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.openid.appauth.*

@AndroidEntryPoint
class AuthorizeFragment : Fragment() {
    private var binding: FragmentAuthorizeBinding? = null
    private val viewModel: AuthViewModel by viewModels()
    private val getAuthResponse = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val dataIntent = it.data ?: return@registerForActivityResult
        handleAuthResponseIntent(dataIntent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuth()
        binding!!.btRegister.setOnClickListener {
            getAuthResponse.launch(viewModel.openLoginPage(requireContext()))
        }
    }

    private fun observeAuth() {
        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.authState.collect { state ->
                    when (state) {
                        is AuthenticationState.LoggedIn -> {
                            binding!!.progressBar.isVisible = false
                            binding!!.btRegister.isVisible = false
                            Toast.makeText(requireContext(), getString(R.string.succes_auth), Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_authorizeFragment_to_navigation_home)
                        }
                        is AuthenticationState.Error -> {
                            binding!!.progressBar.isVisible = false
                            binding!!.btRegister.isVisible = true
                            Log.d(TAG,state.errorMsg)
                        }
                        is AuthenticationState.NotLoggedIn -> {
                            binding!!.progressBar.isVisible = false
                            Log.d(TAG,"not logged in")
                        }
                    }
                }
        }
    }

    private fun openAuthIntent(intent: Intent){
        getAuthResponse.launch(intent)
    }


    private fun handleAuthResponseIntent(intent: Intent){
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()
        when{
            exception != null -> viewModel.changeAuthStateToError(exception.error.toString())
            tokenExchangeRequest != null -> viewLifecycleOwner.lifecycleScope.launch {
                val a = viewModel.getToken(tokenExchangeRequest.authorizationCode.toString())
                SharedPrefToken(requireContext()).saveText(a!!)
                viewModel.changeAuthStateToLoggedIn()
            }
        }

    }
//    private fun observeCode(){
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//                viewModel.code.collect{
//                    when(it){
//                        "123" -> Log.d(TAG,"Что-то не так")
//                        else -> SharedPrefToken(requireContext()).saveText(viewModel.getToken())
//                    }
//                }
//        }
//    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}