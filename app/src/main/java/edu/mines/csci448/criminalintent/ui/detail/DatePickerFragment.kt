package edu.mines.csci448.criminalintent.ui.detail

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.io.Serializable
import java.util.*


private const val ARG_DATE = "date"
private const val RESULT_DATE_KEY = "resultDate"
class DatePickerFragment  : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface Callbacks{
        fun onDateSelected(date:Date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val resultDate: Date = calendar.time
        val result = Bundle().apply {
            putSerializable(RESULT_DATE_KEY, resultDate)
        }
        val resultRequestKey = requireArguments().getString(ARG_REQUEST_DATE, "")
        setFragmentResult(resultRequestKey, result)
    }

    lateinit var calendar:Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        calendar = Calendar.getInstance()
        calendar.time = date

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
                requireContext(),
                this,
                initialYear,
                initialMonth,
                initialDay
        )
    }



    companion object {
        private const val ARG_DATE = "date"
        private const val ARG_REQUEST_DATE = "requestDate"

        fun newInstance(date: Date, requestDate: String): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
                putSerializable(ARG_REQUEST_DATE, requestDate)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
        fun getSelectedDate(result:Bundle): Serializable? {
            return result.getSerializable(RESULT_DATE_KEY)
        }


    }
}