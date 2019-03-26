package com.gamemotion.mydatabase

import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId

import kotlinx.android.synthetic.main.activity_user_main.*

class UserMain : AppCompatActivity() {

    private var database: DatabaseReference? = null
    private var FCMToken: String? = null
    lateinit var key: String
    val miHashMapChild = HashMap<String, Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        database = FirebaseDatabase.getInstance().getReference("/dispositivos")

        btnInicio.setOnClickListener { view ->

            Data.crearUsuario(txtUsername.text.toString() ,android.os.Build.MANUFACTURER+" "+android.os.Build.ID)

            miHashMapChild.put(FCMToken.toString(), Data.usuario)

            database!!.updateChildren(miHashMapChild)

            val intent = Intent(this, ChatZone::class.java)

            startActivity(intent)
        }

        if (savedInstanceState == null) {
            try {
                FCMToken = FirebaseInstanceId.getInstance().token

                key = database!!.push().key!!

            } catch (e: Exception) {}
        }
    }
}
