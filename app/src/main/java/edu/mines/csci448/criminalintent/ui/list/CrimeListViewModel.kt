package edu.mines.csci448.criminalintent.ui.list

import android.content.Context
import androidx.lifecycle.ViewModel
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.data.repo.CrimeRepository

class CrimeListViewModel(private val crimeRepository: CrimeRepository) : ViewModel() {
    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }

}
