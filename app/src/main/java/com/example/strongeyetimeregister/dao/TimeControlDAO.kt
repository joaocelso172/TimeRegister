package com.example.strongeyetimeregister.dao

import android.util.Log
import androidx.annotation.NonNull
import com.example.strongeyetimeregister.model.TimeControl
import com.example.strongeyetimeregister.model.dto.TimeControlDTO
import com.example.strongeyetimeregister.recyclerviewutils.TimeControlRecyclerAdapter
import com.example.strongeyetimeregister.utils.Base64Custom
import com.example.strongeyetimeregister.utils.FirebaseConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TimeControlDAO {

    private val module = "sign_ups"
    private val mAuth: FirebaseAuth = FirebaseConfig.getFirebaseAuth()!!;
    private lateinit var timeControlList: ArrayList<TimeControl>
    private val TAG = "TimeControlDAO"

    var dbUserRegisterRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference().child("users").child(
            Base64Custom.toBase64(mAuth.getCurrentUser()!!.getEmail())
        ).child(module);

    fun addTimeControl(@NonNull timeControl: TimeControl) {
        val timeControlID: String? = dbUserRegisterRef.push().getKey()

        if (timeControlID != null) {
            timeControl.id = timeControlID
            dbUserRegisterRef.child(timeControl.id).setValue(timeControl)
        } else return
    }
    //TODO create method that convert it to TimeControl model
    fun addTimeControl(@NonNull timeControlDTO: TimeControlDTO) {
        val timeControlID: String? = dbUserRegisterRef.push().getKey()

        val timeControl = TimeControl(timeControlDTO.initialTime.toString(),
            timeControlDTO.endTime.toString(), timeControlDTO.desc)

        if (timeControlID != null) {
            timeControl.id = timeControlID
            dbUserRegisterRef.child(timeControl.id).setValue(timeControl)
        } else return
    }

    fun deleteTimeControl() {}

    fun editTimeControl() {}

    fun getAllTimeControl(tmList: ArrayList<TimeControl>, rcAdapter: TimeControlRecyclerAdapter){

        tmList.clear()
        rcAdapter.notifyDataSetChanged()
        
        dbUserRegisterRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Para cada informação disponivel, executa looping
                for (dados in snapshot.getChildren()) {
                    var timeControl: TimeControl? = dados.getValue(TimeControl::class.java)
                    timeControl!!.id = dados.key.toString()
                    tmList.add(timeControl)

//                    Log.d(TAG, "Time control: $timeControl, from list: ${timeControlList}, from datasnapshot: $snapshot")

                }

                rcAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


}