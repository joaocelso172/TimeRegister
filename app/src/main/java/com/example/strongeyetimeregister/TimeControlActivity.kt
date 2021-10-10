package com.example.strongeyetimeregister

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.strongeyetimeregister.Constants.LISTENER_OFF
import com.example.strongeyetimeregister.Constants.REGISTER_TIME
import com.example.strongeyetimeregister.dao.TimeControlDAO
import com.example.strongeyetimeregister.model.TimeControl
import com.example.strongeyetimeregister.recyclerviewutils.TimeControlRecyclerAdapter

class TimeControlActivity : AppCompatActivity(), View.OnClickListener{

    private val TAG = "TimeControlActivity"
    private lateinit var timeControlRecyclerAdapter: TimeControlRecyclerAdapter
    private lateinit var swtStartCount: Switch
    private lateinit var txtInitialTime: TextView
    private lateinit var txtActualTime: TextView
    private lateinit var txtDurationTime: TextView
    private lateinit var clockListener: ClockListener
    private lateinit var timeControlList: ArrayList<TimeControl>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clockListener = ClockListener(this)
        getTimeControlList()
        swtStartCount = findViewById(R.id.switch_start_count)
        swtStartCount.setOnClickListener(this);

        txtInitialTime = findViewById(R.id.txt_initial_time)
        txtActualTime = findViewById(R.id.txt_actual_time)
        txtDurationTime = findViewById(R.id.txt_duration_time)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        var rcView = findViewById<RecyclerView>(R.id.recycler_view)
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = timeControlRecyclerAdapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
            when(v!!.id!!){
                R.id.switch_start_count -> {
                    if (swtStartCount.isChecked) clockListener.showDateInfo(txtInitialTime, txtActualTime, txtDurationTime)
                    else {
                        standardInfoLayout()
                        clockListener.addTimeRegister()
                    }
                }
        }
    }

    fun getTimeControlList(){
        var timeControlDAO = TimeControlDAO()
        timeControlList = ArrayList()
        timeControlRecyclerAdapter = TimeControlRecyclerAdapter(timeControlList);
        timeControlDAO.getAllTimeControl(timeControlList, timeControlRecyclerAdapter)
    }

    fun standardInfoLayout(){
        txtActualTime.text = "00:00:00"
        txtDurationTime.text = "00:00:00"
        txtInitialTime.text = "00:00:00"
    }


}