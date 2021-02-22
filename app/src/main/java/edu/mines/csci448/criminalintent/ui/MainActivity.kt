package edu.mines.csci448.criminalintent.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import edu.mines.csci448.criminalintent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "448.MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO 2 create fragment transaction
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }
}