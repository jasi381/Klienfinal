package com.example.klienfinal.viewmodel.lunchapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.klienfinal.firebaseDatabase.FirebaseDb

class ViewModelProviderFactory(
    private val firebaseDb: FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KleineViewModel(firebaseDb) as T
    }
}