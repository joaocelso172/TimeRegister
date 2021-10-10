package com.example.strongeyetimeregister.model

data class User(
    var name: String,
    var email: String,
){
    constructor() : this("", "")
}