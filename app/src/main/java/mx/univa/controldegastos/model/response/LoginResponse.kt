package mx.univa.controldegastos.model.response

import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.Usuario

class LoginResponse: Respuesta() {

    var usuario: Usuario = Usuario()
        get()=field
        set(value){field = value}
}