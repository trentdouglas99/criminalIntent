package edu.mines.csci448.criminalintent.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.format
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import edu.mines.csci448.criminalintent.R
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.databinding.FragmentDetailBinding
import edu.mines.csci448.criminalintent.ui.TimePickerFragment
import edu.mines.csci448.criminalintent.ui.list.CrimeDetailViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
// CrimeDetailFragment.kt
private const val REQUIRED_CONTACTS_PERMISSION = android.Manifest.permission.READ_CONTACTS

class CrimeDetailFragment : Fragment(), DatePickerFragment.Callbacks, FragmentResultListener {


    // CrimeDetailFragment.kt
    private fun hasContactsPermission() =
            ContextCompat.checkSelfPermission(requireContext(), REQUIRED_CONTACTS_PERMISSION) == PackageManager.PERMISSION_GRANTED


    private val args: CrimeDetailFragmentArgs by navArgs()
    private lateinit var crimeDetailViewModel: CrimeDetailViewModel
    companion object {
        private const val LOG_TAG = "448.CrimeDetailFrag"
        private const val ARG_CRIME_ID = "crimeId"
        fun createFragment(crimeId: UUID): CrimeDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeDetailFragment().apply {
                arguments = args
            }
        }

    }

    private fun generateCrimeReport(): String {
        val dateString = format("EEE, MMM dd", crime.date)
        val solvedString = if(crime.isSolved) { getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val suspectString = if(crime.suspect == null) { getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspectString)
    }

    private lateinit var pickContactContract: ActivityResultContract<Uri, Uri?>
    private lateinit var pickContactCallback: ActivityResultCallback<Uri?>
    private lateinit var pickContactLauncher: ActivityResultLauncher<Uri>
    private lateinit var pickContactPermissionCallback: ActivityResultCallback<Boolean>
    private lateinit var pickContactPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var reportButton:Button

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
            REQUEST_TIME -> {
                crime.date = TimePickerFragment.getSelectedDate(result) as Date
                updateUI()
            }
        }
    }


    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    private var calendar : Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?){



        Log.d(LOG_TAG, "onCreate() called")
        crime = Crime()

        pickContactContract = object : ActivityResultContract<Uri, Uri?>() { override fun createIntent(context: Context, input: Uri): Intent {
            Log.d(LOG_TAG, "createIntent() called")
            return Intent(Intent.ACTION_PICK, input)
        }
            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                Log.d(LOG_TAG, "parseResult() called")
                if(resultCode != Activity.RESULT_OK || intent == null) return null
            return intent.data
            }
        }

        super.onCreate(savedInstanceState)
        val crimeId = args.crimeId
        Log.d(LOG_TAG, "args bundle crime ID: $crimeId")

        val factory = CrimeDetailViewModelFactory(requireContext())
        crimeDetailViewModel = ViewModelProvider(this, factory).get(CrimeDetailViewModel::class.java)
        crimeDetailViewModel.loadCrime(crimeId)

        pickContactCallback = ActivityResultCallback<Uri?> { contactUri: Uri? -> Log.d(LOG_TAG, "onActivityResult() called with result: $contactUri")
            // CrimeDetailFragment.kt where “Part IV code goes here” comment was
            if(contactUri != null) {
// specify which fields you want your query to return values for
                //val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME) // perform your query, the contactUri is like a "where" clause
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
                val cursor = requireActivity().contentResolver.query(contactUri,
                queryFields,
                null, null, null)
                cursor?.use {contactIter ->
// double check that you got results
                    if(contactIter.count > 0) {
// pull out the first column of the first row of data
    // that is the contact's name
                    contactIter.moveToFirst()
                    val suspect = contactIter.getString(0)
                    crime.suspect = suspect             // set the crime's suspect field
                    crimeDetailViewModel.saveCrime(crime)// save the crime
                        // pull out the second column – that is the contact's ID
                        val contactID = contactIter.getString(1)
                        val phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        val phoneNumberQueryFields = arrayOf(
                                ContactsContract.CommonDataKinds.Phone.NUMBER )
                        val whereClause = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
                        val phoneQueryParameters = arrayOf( contactID.toString() )
                        val phoneCursor = requireActivity()
                                .contentResolver
                                .query(phoneURI, phoneNumberQueryFields,
                                        whereClause, phoneQueryParameters,
                                        null)
                        phoneCursor?.use { phoneIter ->
                            if( phoneIter.count > 0 ) {
                                // phone number found
                                phoneIter.moveToFirst()
                                crime.suspectNumber = phoneIter.getString(0)
                                binding.crimeCallButton.isEnabled = true
                            } else {
                                // no phone number found
                                crime.suspectNumber = null
                                binding.crimeCallButton.isEnabled = false
                            }
                        }

                    }
                }
            }

            crimeDetailViewModel.saveCrime(crime)

        }


        pickContactPermissionCallback = ActivityResultCallback { isGranted: Boolean ->
            if(isGranted) {
                // permission is granted
                Log.d(LOG_TAG, "permission granted!")
                // make sure button is enabled and launch the pickContact intent
                binding.crimeSuspectButton.isEnabled = true

            } else {
                // permission denied, explain to the user
                Log.d(LOG_TAG, "permission denied")
                binding.crimeSuspectButton.isEnabled = false
                Toast.makeText(requireContext(),
                        R.string.crime_reason_for_contacts,
                        Toast.LENGTH_LONG).show()
            } }
        pickContactPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission(),
                        pickContactPermissionCallback)

        pickContactLauncher = registerForActivityResult(pickContactContract, pickContactCallback)
        val pickContactIntent = pickContactContract.createIntent(requireContext(), ContactsContract.Contacts.CONTENT_URI)


    }

    private var _binding: FragmentDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{


        if( !hasContactsPermission() ) {
            Log.d(LOG_TAG, "user has NOT granted permission to access contacts")
        } else {
            Log.d(LOG_TAG, "user has granted permission to access contacts")
                    // this is where this goes pickContactLauncher.launch(ContactsContract.Contacts.CONTENT_URI)
        }


        Log.d(LOG_TAG, "onCreateView() called")

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        titleField = binding.crimeTitleEditText
        dateButton = binding.crimeDateButton
        timeButton = binding.crimeTimeButton
        solvedCheckBox = binding.crimeSolvedCheckbox
        reportButton = binding.crimeReportButton

        val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        //pickContactIntent.addCategory(Intent.CATEGORY_HOME)
        val contactPickerAvailable = requireActivity().packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY) != null

        binding.crimeSuspectButton.apply {
            setOnClickListener {
                pickContactLauncher.launch(ContactsContract.Contacts.CONTENT_URI)
                if( !hasContactsPermission() ) {
                    Log.d(LOG_TAG, "user has NOT granted permission to access contacts")
                    // should we show an explanation?
                    if( ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_CONTACTS_PERMISSION ) ) { Log.d(LOG_TAG, "show an explanation")
                        Toast.makeText(requireContext(),R.string.crime_reason_for_contacts,Toast.LENGTH_LONG).show()
                    } else {
                        Log.d(LOG_TAG, "no explanation needed, request permission")
                        pickContactPermissionLauncher.launch(REQUIRED_CONTACTS_PERMISSION)
                    }
                }
            }
            visibility = if(contactPickerAvailable) {
                View.VISIBLE
            } else {
                View.GONE }
        }
        binding.crimeCallButton.apply {
            visibility = if(contactPickerAvailable) {
                View.VISIBLE
            } else {
                View.GONE }
        }

        binding.crimeCallButton.setOnClickListener {
            val phoneNumberURI = Uri.parse( "tel:${crime.suspectNumber}" )
            val callIntent = Intent(Intent.ACTION_DIAL, phoneNumberURI)
            startActivity( callIntent )
        }


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
        childFragmentManager.setFragmentResultListener(REQUEST_TIME, viewLifecycleOwner, this)

        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner, Observer { crime ->
                crime?.let {
                    this.crime = crime
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
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int){
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply{
            setOnCheckedChangeListener{ _, isChecked ->crime.isSolved = isChecked}
        }
        dateButton.setOnClickListener{
            DatePickerFragment.newInstance(crime.date, REQUEST_DATE).show(
                childFragmentManager,
                REQUEST_DATE
            )
        }
        timeButton.setOnClickListener{
            TimePickerFragment.newInstance(crime.date, REQUEST_TIME).show(
                childFragmentManager,
                REQUEST_TIME
            )
        }
        reportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, generateCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject))
            }
            startActivity(intent)
        }



        calendar.time = crime.date


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
        binding.crimeTitleEditText.setText(crime.title)

        //var dateString: String = crime.date.day.toString() + " " + crime.date.month.toString() + " " + crime.date.year.toString()
        val df: DateFormat = SimpleDateFormat("MM/dd/yy")
        val tf: DateFormat = SimpleDateFormat("HH:mm")
        binding.crimeDateButton.text = df.format(crime.date)
        binding.crimeTimeButton.text = tf.format(crime.date)
        //binding.crimeSolvedCheckbox.isChecked = crime.isSolved
        binding.crimeSolvedCheckbox.apply{
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }

        binding.crimeCallButton.isEnabled = crime.suspectNumber != null

        if(crime.suspect != null ) {
            binding.crimeSuspectButton.text = crime.suspect
        }

    }
}