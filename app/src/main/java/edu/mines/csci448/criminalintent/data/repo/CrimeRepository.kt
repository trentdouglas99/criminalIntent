package edu.mines.csci448.criminalintent.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.data.db.CrimeDao
import edu.mines.csci448.criminalintent.data.db.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*


class CrimeRepository private constructor (private val crimeDao: CrimeDao) {
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)
    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun getInstance(context: Context): CrimeRepository {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    val database = CrimeDatabase.getInstance(context)
                    instance = CrimeRepository(database.crimeDao)
                    INSTANCE = instance
                }
                return instance
            }
        }

    }


}
