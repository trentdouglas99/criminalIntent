package edu.mines.csci448.criminalintent.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.databinding.ListItemCrimeBinding

// CrimeHolder.kt
class CrimeHolder(val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var crime: Crime

    fun bind(crime: Crime, clickListener: (Crime) -> Unit ) {
        this.crime = crime
        itemView.setOnClickListener { clickListener(this.crime) }
        binding.crimeTitleTextView.text = this.crime.title
        binding.crimeDateTextView.text = this.crime.date.toString()
        binding.crimeSolvedImageView.visibility = if(this.crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}