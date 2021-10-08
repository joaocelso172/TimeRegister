package com.example.strongeyetimeregister.model.dto

import java.time.LocalDateTime

data class TimeControlDTO (var initialTime: LocalDateTime, var endTime: LocalDateTime, var desc: String, var id: String) {

    constructor(initialTime: LocalDateTime, endTime: LocalDateTime) : this(initialTime, endTime, "", "")
}