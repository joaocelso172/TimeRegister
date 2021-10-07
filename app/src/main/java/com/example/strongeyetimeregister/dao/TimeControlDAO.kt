package com.example.strongeyetimeregister.dao

import android.util.Log
import androidx.annotation.NonNull
import com.example.strongeyetimeregister.model.TimeControl
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

    fun deleteTimeControl() {}

    fun editTimeControl() {}

    fun getAllTimeControl(tmList: ArrayList<TimeControl>, rcAdapter: TimeControlRecyclerAdapter) : ArrayList<TimeControl>{
        timeControlList = ArrayList<TimeControl>()

        dbUserRegisterRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Para cada informação disponivel, executa looping

                //Para cada informação disponivel, executa looping
                for (dados in snapshot.getChildren()) {
                    var timeControl: TimeControl? = snapshot.getValue(TimeControl::class.java)
                    timeControl!!.id = dados.key.toString()
                    timeControl.let { tmList.add(it) }

                }

                rcAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return timeControlList
    }


}