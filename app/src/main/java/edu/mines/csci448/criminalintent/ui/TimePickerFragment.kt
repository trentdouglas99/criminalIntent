package edu.mines.csci448.criminalintent.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import edu.mines.csci448.criminalintent.ui.detail.DatePickerFragment
import java.io.Serializable
import java.sql.Time
import java.util.*
import kotlin.math.min

private const val RESULT_TIME_KEY = "resultTime"
class TimePickerFragment:DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var calendar: Calendar
    private lateinit var date : Date
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        date = arguments?.getSerializable(ARG_TIME) as Date
        calendar = Calendar.getInstance()
        calendar.time = date
        val initialHours:Int = calendar.time.hours
        val initialMinutes:Int = calendar.time.minutes

        return TimePickerDialog(
            requireContext(),
            this,
            initialHours,
            initialMinutes,
            false
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.time = date
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute)
        val resultTime = calendar.time
        val result = Bundle().apply{
            putSerializable(RESULT_TIME_KEY, resultTime)
        }
        val resultRequestKey = requireArguments().getString(ARG_REQUEST_TIME, "")
        setFragmentResult(resultRequestKey, result)

    }

    companion object {
        private const val ARG_TIME = "time"
        private const val ARG_REQUEST_TIME = "requestTime"

        fun newInstance(date: Date, requestTime: String): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, date)
                putSerializable(ARG_REQUEST_TIME, requestTime)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }
        fun getSelectedDate(result:Bundle): Serializable? {
            return result.getSerializable(RESULT_TIME_KEY)
        }


    }

}