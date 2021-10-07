package com.example.strongeyetimeregister.model

data class User(
    var name: String,
    var email: String,
    var isSignedUp: Boolean
){
    constructor() : this("", "", false)
}