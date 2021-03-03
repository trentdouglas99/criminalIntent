package edu.mines.csci448.criminalintent.ui.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.databinding.FragmentDetailBinding
import edu.mines.csci448.criminalintent.ui.list.CrimeDetailViewModelFactory
import java.util.*


private const val REQUEST_DATE = "DialogDate"

class CrimeDetailFragment : Fragment(), DatePickerFragment.Callbacks, FragmentResultListener {
    private val args: CrimeDetailFragmentArgs by navArgs()
    private lateinit var crimeDetailViewModel: CrimeDetailViewModel
    companion object {
        private const val LOG_TAG = "448.CrimeDetailFrag"
    }
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onAttach(context: Context){
        Log.d(LOG_TAG, "onAttach() called")
        super.onAttach(context)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_DATE -> {
                crime.date = DatePickerFragment.getSelectedDate(result) as Date
                updateUI()
            }
        }
    }


    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onCreate() called")
        crime = Crime()
        super.onCreate(savedInstanceState)
        val crimeId = args.crimeId
        Log.d(LOG_TAG, "args bundle crime ID: $crimeId")

        val factory = CrimeDetailViewModelFactory(requireContext())
        crimeDetailViewModel = ViewModelProvider(this, factory).get(CrimeDetailViewModel::class.java)
        crimeDetailViewModel.loadCrime(crimeId)
    }

    private var _binding: FragmentDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        Log.d(LOG_TAG, "onCreateView() called")

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        titleField = binding.crimeTitleEditText
        dateButton = binding.crimeDateButton
        solvedCheckBox = binding.crimeSolvedCheckbox
//        dateButton.apply{
//            text = crime.date.toString()
//            isEnabled = false
//        }
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onViewCreated() called")
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)

        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner, Observer { crime ->
                crime?.let{
                    this.crime=crime
                    updateUI()
                }
            })



    }
    override fun onActivityCreated(savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onActivityCreated() called")
        super.onActivityCreated(savedInstanceState)
    }
    override fun onStart(){
        Log.d(LOG_TAG, "onStart() called")
        super.onStart()

        val titleWatcher = object : TextWatcher{
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(sequence: CharSequence?, start:Int, before:Int, count:Int){
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply{
            setOnCheckedChangeListener{_, isChecked ->crime.isSolved = isChecked}
        }
        dateButton.setOnClickListener{
            DatePickerFragment.newInstance(crime.date, REQUEST_DATE).show(childFragmentManager, REQUEST_DATE)
        }


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

        crimeDetailViewModel.saveCrime(crime)
    }
    override fun onDestroyView(){
        Log.d(LOG_TAG, "onDestroyView() called")
        _binding = null
        super.onDestroyView()
    }
    override fun onDestroy(){
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }
    override fun onDetach(){
        Log.d(LOG_TAG, "onDetach() called")
        super.onDetach()
    }

    private fun updateUI() {
        binding.crimeTitleEditText.setText( crime.title )
        binding.crimeDateButton.text = crime.date.toString()
        //binding.crimeSolvedCheckbox.isChecked = crime.isSolved
        binding.crimeSolvedCheckbox.apply{
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }
}