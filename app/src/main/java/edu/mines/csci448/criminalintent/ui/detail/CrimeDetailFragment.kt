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
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.databinding.FragmentDetailBinding

class CrimeDetailFragment : Fragment() {
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
    override fun onCreate(savedInstanceState: Bundle?){
        Log.d(LOG_TAG, "onCreate() called")
        crime = Crime()
        super.onCreate(savedInstanceState)
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
        dateButton.apply{
            text = crime.date.toString()
            isEnabled = false
        }
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
}