package edu.mines.csci448.criminalintent.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.mines.csci448.criminalintent.data.Crime

@Database(entities = [ Crime::class ], version=1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract val crimeDao: CrimeDao
    companion object {
        private const val DATABASE_NAME = "crime-database"
        private var INSTANCE: CrimeDatabase? = null
        fun getInstance(context: Context): CrimeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        CrimeDatabase::class.java,
                        DATABASE_NAME)
                        .build()
                }
                return instance
            }
        }
    }
}