package com.example.strongeyetimeregister.model

import android.os.Build
import androidx.annotation.RequiresApi

data class TimeControl(var initialTime: String, var endTime: String, var desc: String, var id: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this("", "", "", "")

    constructor(initialTime: String, endTime: String, desc: String) : this(initialTime, endTime, desc, "")

    constructor(initialTime: String, endTime: String) : this("", "", "", "")

}