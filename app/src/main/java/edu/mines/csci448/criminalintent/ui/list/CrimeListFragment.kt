package edu.mines.csci448.criminalintent.ui.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.mines.csci448.criminalintent.data.Crime
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import edu.mines.csci448.criminalintent.R
import edu.mines.csci448.criminalintent.databinding.FragmentListBinding


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
        setHasOptionsMenu(true)
        val factory = CrimeListViewModelFactory(requireContext())
        crimeListViewModel = ViewModelProvider(this@CrimeListFragment, factory).get(CrimeListViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(LOG_TAG, "onOptionsItemSelected() called")
        return when(item.itemId) {
            R.id.new_crime_menu_item -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                val action = CrimeListFragmentDirections
                        .actionCrimeListFragmentToCrimeDetailFragment(crime.id)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(LOG_TAG, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!
    private lateinit var crimeListViewModel: CrimeListViewModel

    private lateinit var adapter: CrimeListAdapter

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeListAdapter(crimes) {
                crime: Crime -> Unit
                val action = CrimeListFragmentDirections.actionCrimeListFragmentToCrimeDetailFragment( crime.id )
                findNavController().navigate(action)
        }




        binding.crimeListRecyclerView.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        Log.d(LOG_TAG, "onCreateView() called")
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.crimeListRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI(emptyList())

        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onViewCreated() called")
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let{
                    Log.i(LOG_TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            }
        )
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
