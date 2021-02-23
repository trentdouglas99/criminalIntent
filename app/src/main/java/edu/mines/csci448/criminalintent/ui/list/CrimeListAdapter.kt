package edu.mines.csci448.criminalintent.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.criminalintent.data.Crime
import edu.mines.csci448.criminalintent.databinding.ListItemCrimeBinding

// CrimeListAdapter.kt
class CrimeListAdapter(private val crimes: List<Crime>, private val clickListener: (Crime) -> Unit ) : RecyclerView.Adapter<CrimeHolder>() {

    override fun getItemCount(): Int{
        return crimes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val binding = ListItemCrimeBinding.inflate( LayoutInflater.from(parent.context), parent, false )
        return CrimeHolder(binding)

    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime, clickListener)
    }

}