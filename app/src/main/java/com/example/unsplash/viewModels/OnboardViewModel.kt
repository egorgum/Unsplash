package com.example.unsplash.viewModels

import androidx.lifecycle.ViewModel
import com.example.unsplash.R
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class OnboardViewModel @Inject constructor(): ViewModel() {
    var text: MutableStateFlow<Int> = MutableStateFlow(R.string.firs_recommendation)
}