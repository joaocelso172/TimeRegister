package com.example.strongeyetimeregister.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.util.*

data class TimeControl public constructor(var initialTime: String, var endTime: String, var desc: String, var id: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this("", "", "", "")

    constructor(initialTime: LocalDateTime, endTime: LocalDateTime, desc: String) : this("", "", desc, "")

    constructor(initialTime: LocalDateTime, endTime: LocalDateTime) : this("", "", "", "")

}