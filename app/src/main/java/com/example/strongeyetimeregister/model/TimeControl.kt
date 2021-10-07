package com.example.strongeyetimeregister.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

data class TimeControl public constructor(var initialTime: String, var endTime: String, var desc: String, var id: String) {

    constructor() : this("", "", "", "")

    constructor(initialTime: String, endTime: String, desc: String) : this(initialTime, endTime, desc, "")

}