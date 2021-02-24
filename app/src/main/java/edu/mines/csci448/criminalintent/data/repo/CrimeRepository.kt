package edu.mines.csci448.criminalintent.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.data.db.CrimeDao
import edu.mines.csci448.criminalintent.data.db.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors


class CrimeRepository private constructor (private val crimeDao: CrimeDao) {
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)
    private val executor = Executors.newSingleThreadExecutor()
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
    fun addCrime(crime: Crime){
        executor.execute{
            crimeDao.addCrime(crime)
        }
    }
    fun updateCrime(crime: Crime){
        executor.execute{
            crimeDao.updateCrime(crime)
        }
    }


}
