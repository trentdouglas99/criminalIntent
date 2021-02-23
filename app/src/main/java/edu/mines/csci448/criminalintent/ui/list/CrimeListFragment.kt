package edu.mines.csci448.criminalintent.ui.list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.databinding.ActivityMainBinding
import edu.mines.csci448.criminalintent.databinding.FragmentDetailBinding
import edu.mines.csci448.criminalintent.databinding.FragmentListBinding
import edu.mines.csci448.criminalintent.ui.detail.CrimeDetailFragment


class CrimeListFragment : Fragment() {

    companion object {
        private const val LOG_TAG = "448.CrimeListFrag"
    }

    override fun onAttach(context: Context){
        Log.d(LOG_TAG, "onAttach() called")
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onCreate() called")
        super.onCreate(savedInstanceState)

        val factory = CrimeListViewModelFactory()
        crimeListViewModel = ViewModelProvider(this@CrimeListFragment, factory).get(CrimeListViewModel::class.java)
    }

    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!
    private lateinit var crimeListViewModel: CrimeListViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        Log.d(LOG_TAG, "onCreateView() called")
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.crimeListRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onViewCreated() called")
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onActivityCreated() called")
        super.onActivityCreated(savedInstanceState)
    }
    override fun onStart(){
        Log.d(LOG_TAG, "onStart() called")
        super.onStart()

    }
    override fun onResume(){
        Log.d(LOG_TAG, "onResume() called")
        super.onResume()
    }
    override fun onPause(){
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }
    override fun onStop(){
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }
    override fun onDestroyView(){
        Log.d(LOG_TAG, "onDestroyView() called")
        super.onDestroyView()
    }
    override fun onDestroy(){
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
        _binding = null
    }
    override fun onDetach(){
        Log.d(LOG_TAG, "onDetach() called")
        super.onDetach()
    }


}
