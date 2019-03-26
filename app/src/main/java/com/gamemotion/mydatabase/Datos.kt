package com.gamemotion.mydatabase

import java.util.*

object Data {
    val usuario = HashMap<String, Any>()
    val mensaje = HashMap<String, Any>()

    var user: String = ""

    fun crearUsuario(user: String, nombreDispositivo: String) {
        usuario.put("username", user)
        usuario.put("nombre", nombreDispositivo)

        this.user = user
    }

    fun crearMensaje(token: String = "", msg: String = "") {
        mensaje.put("tokenUsuario", user)
        mensaje.put("mensaje", msg)
    }


}