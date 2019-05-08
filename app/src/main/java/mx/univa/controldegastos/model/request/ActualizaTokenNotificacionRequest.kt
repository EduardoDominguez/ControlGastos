package mx.univa.controldegastos.model.request

class ActualizaTokenNotificacionRequest {
    var IdUsuario : Int = 0
        get()=field
        set(value){field = value}

    var Token : String = ""
        get()=field
        set(value){field = value}
}