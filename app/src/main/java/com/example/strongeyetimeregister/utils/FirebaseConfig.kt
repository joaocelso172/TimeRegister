package com.example.strongeyetimeregister.utils

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class FirebaseConfig {


    companion object{
        private var mAuth: FirebaseAuth? = null

        fun getFirebaseAuth(): FirebaseAuth? {
            if (mAuth == null) {
                mAuth = FirebaseAuth.getInstance()
            }
            return mAuth
        }
    }

}