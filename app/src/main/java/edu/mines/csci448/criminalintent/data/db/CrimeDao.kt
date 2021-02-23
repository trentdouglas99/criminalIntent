package edu.mines.csci448.criminalintent.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import edu.mines.csci448.criminalintent.data.Crime
import java.util.*

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>
}