package edu.mines.csci448.criminalintent.ui
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import edu.mines.csci448.criminalintent.ui.detail.CrimeDetailFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewbinding.ViewBinding
import edu.mines.csci448.criminalintent.CriminalIntentApplication
import edu.mines.csci448.criminalintent.databinding.ActivityMainBinding
import edu.mines.csci448.criminalintent.databinding.ActivityTwoPaneBinding
import edu.mines.csci448.criminalintent.ui.list.CrimeListFragment
import java.util.*

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {

        override fun getOrientation() = initialOrientation
        override fun onCrimeSelected(crimeId: UUID) {
            Log.d(LOG_TAG, "onCrimeSelected() called!")
            // make sure to only do this in landscape mode
            if(initialOrientation == ORIENTATION_LANDSCAPE) {
                val detailFragment = CrimeDetailFragment.createFragment(crimeId)
                supportFragmentManager.beginTransaction().replace((binding as ActivityTwoPaneBinding).detailFragmentContainer.id, detailFragment).commit()
            }

        }


        private val initialOrientation by lazy {
        (application as CriminalIntentApplication).initialOrientation
    }

    companion object {
        private const val LOG_TAG = "448.MainActivity"
    }

    private lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")

        //binding = ActivityMainBinding.inflate(layoutInflater)
        binding = if(initialOrientation == ORIENTATION_PORTRAIT) {
            ActivityMainBinding.inflate(layoutInflater)
        } else {
            ActivityTwoPaneBinding.inflate(layoutInflater)
        }


        setContentView(binding.root)

        //val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        if(initialOrientation == ORIENTATION_PORTRAIT) {
            val navHostFragment = supportFragmentManager.findFragmentById((binding as ActivityMainBinding).navHostFragment.id) as NavHostFragment
            NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)
        }
        else if(initialOrientation == ORIENTATION_LANDSCAPE) {
            // manually set up fragment transaction as we did back in Lab5A/B/C
            var listFragment = supportFragmentManager
                    .findFragmentById((binding as ActivityTwoPaneBinding).listFragmentContainer.id)
            if(listFragment == null) {
                listFragment = CrimeListFragment()
                supportFragmentManager
                        .beginTransaction()
                        .add((binding as ActivityTwoPaneBinding).listFragmentContainer.id, listFragment)
                        .commit()
            }
        }






    }


    override fun onSupportNavigateUp(): Boolean {
        if(initialOrientation == ORIENTATION_PORTRAIT){
            return findNavController((binding as ActivityMainBinding).navHostFragment.id).navigateUp() || super.onSupportNavigateUp()
        }
        else{
            //probably wrong...
            return false
        }

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