package edu.mines.csci448.criminalintent.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.mines.csci448.criminalintent.data.Crime
@Database(entities = [ Crime::class ], version = 2)
//@Database(entities = [ Crime::class ], version=1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract val crimeDao: CrimeDao
    companion object {
        private const val DATABASE_NAME = "crime-database"
        private var INSTANCE: CrimeDatabase? = null

        private val migration_1_2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) { database.execSQL( "ALTER TABLE crime ADD COLUMN suspect TEXT DEFAULT NULL")
                database.execSQL( "ALTER TABLE crime ADD COLUMN suspectNumber TEXT DEFAULT NULL")
            }
        }


        fun getInstance(context: Context): CrimeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        CrimeDatabase::class.java,
                        DATABASE_NAME)
                            .addMigrations(migration_1_2)
                            .build()
                }
                return instance
            }
        }
    }
}