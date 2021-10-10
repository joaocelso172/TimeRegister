package com.example.strongeyetimeregister.recyclerviewutils

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.strongeyetimeregister.DateConverter
import com.example.strongeyetimeregister.R
import com.example.strongeyetimeregister.model.TimeControl
import java.time.Duration
import java.time.LocalDateTime

class TimeControlRecyclerAdapter (private val timeControlList: ArrayList<TimeControl>) :
    RecyclerView.Adapter<TimeControlRecyclerAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDescName: TextView = itemView.findViewById(R.id.txt_desc_name)
        val txtInitTime: TextView = itemView.findViewById(R.id.txt_init_date)
        val txtEndTime: TextView = itemView.findViewById(R.id.txt_end_date)
        val txtDurationTime: TextView = itemView.findViewById(R.id.txt_duration_sprint)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.time_control_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val initLocalDateTime = DateConverter.stringToLocalDateTime(timeControlList[position].initialTime)
        val endLocalDateTime = DateConverter.stringToLocalDateTime(timeControlList[position].endTime)
        val duration = Duration.between(initLocalDateTime, endLocalDateTime)

        viewHolder.txtDescName.text = if (timeControlList[position].desc.equals("")) "----------" else timeControlList[position].desc
        viewHolder.txtInitTime.text = (DateConverter.completeDateToDisplayDate(initLocalDateTime))
        viewHolder.txtEndTime.text = (DateConverter.completeDateToDisplayDate(endLocalDateTime))
        viewHolder.txtDurationTime.text = (DateConverter.durationToHours(duration))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = timeControlList.size
}