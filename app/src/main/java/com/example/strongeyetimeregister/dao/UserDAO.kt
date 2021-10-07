package com.example.strongeyetimeregister.dao

import android.content.Context
import android.util.Log
import com.example.strongeyetimeregister.model.User
import com.example.strongeyetimeregister.utils.Base64Custom
import com.example.strongeyetimeregister.utils.FirebaseConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserDAO constructor(c: Context){
    private var mAuth: FirebaseAuth = FirebaseConfig.getFirebaseAuth()!!;
    private var dbUserRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    private var user: User = User("", "", false)
    private var success: Boolean = false
    private var dbUserChildRef: DatabaseReference? = null

    fun validateOrSubUsuario(): Boolean? {
        //Codifica ID do usuário
        val idUsuario: String =
            Base64Custom.toBase64(mAuth.getCurrentUser()!!.getEmail())
        //Usa um dbReference novo (usuariosAdd para receber o valor usuariosRef.chil) pode ser alterado. Passa como parametro o email codificado
        dbUserChildRef = dbUserRef.child(idUsuario)
        //Inicia o eventListener para buscar usuários
        dbUserChildRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Se existir informação com o email codificado, pega valor
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User::class.java)!!
                    success = true
                } //Se não, cadastra
                else {
                    Log.i("Logando", "Usuarionão cadastrado")
                    success = dbUserRef.child(idUsuario)
                        .setValue(user).isSuccessful()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return success
    }
}