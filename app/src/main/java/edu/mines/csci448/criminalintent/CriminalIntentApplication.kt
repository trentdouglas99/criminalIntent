package edu.mines.csci448.criminalintent

import android.app.Application
import android.util.Log
import edu.mines.csci448.criminalintent.data.repo.CrimeRepository
import edu.mines.csci448.criminalintent.ui.MainActivity

class CriminalIntentApplication: Application() {
    companion object {
        private const val LOG_TAG = "448.criminalIntentApplication"
    }
    override fun onCreate(){
        super.onCreate()
        Log.d(LOG_TAG, "onCreate() called")
        CrimeRepository.getInstance(this)
    }
}