package edu.mines.csci448.criminalintent.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// CrimeListViewModelFactory.kt
class CrimeListViewModelFactory : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }

}