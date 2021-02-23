package edu.mines.csci448.criminalintent.ui.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.mines.csci448.criminalintent.data.repo.CrimeRepository

// CrimeListViewModelFactory.kt
class CrimeListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CrimeRepository::class.java).newInstance(CrimeRepository.getInstance(context))
    }

}