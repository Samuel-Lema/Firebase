package com.gamemotion.mydatabase

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_chat_zone.*
import kotlinx.android.synthetic.main.content_chat_zone.*

class ChatZone : AppCompatActivity() {

    private var database: DatabaseReference? = null
    private var FCMToken: String? = null
    lateinit var key: String
    val miHashMapChild = HashMap<String, Any>()
    val messages =  HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_zone)
        setSupportActionBar(toolbar)

        // Guardo la referencia de los mensajes de Firebase
        database = FirebaseDatabase.getInstance().getReference("/mensajes")

        // Inicio el listener del boton que permite enviar los mensajes en el chat
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Mensaje enviado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            miHashMapChild.clear()

            FCMToken = FirebaseInstanceId.getInstance().token
            key = database!!.push().key!!

            Data.crearMensaje(FCMToken.toString(), inputChat.text.toString())

            miHashMapChild.put(key, Data.mensaje)
            database!!.updateChildren(miHashMapChild)
        }

        outputChat.setMovementMethod(ScrollingMovementMethod())
        initListener()
    }

    // Inicio el listener para que se sincronize con Firebase en algunas funciones
    private fun initListener() {
        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                messages.put("${p0.key}", "${p0.child("tokenUsuario").getValue()} / ${p0.child("mensaje").getValue().toString()}")

                outputChat.text = ""

                for (msg in messages){

                    outputChat.text = "${outputChat.text} ${msg.value.split(" / ")[0]}: ${msg.value.split(" / ")[1]}\n"
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                messages.remove(p0.key)

                outputChat.text = ""

                for (msg in messages){

                    outputChat.text = "${outputChat.text} ${msg.value.split(" / ")[0]}: ${msg.value.split(" / ")[1]}\n"
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                // Guarda el mensaje en el buffer local
                messages.put("${p0.key}", "${p0.child("tokenUsuario").getValue()} / ${p0.child("mensaje").getValue().toString()}")

                outputChat.text = ""

                for (msg in messages){

                    outputChat.text = "${outputChat.text} ${msg.value.split(" / ")[0]}: ${msg.value.split(" / ")[1]}\n"
                }
            }
        }

        database!!.addChildEventListener(childEventListener)
    }
}
