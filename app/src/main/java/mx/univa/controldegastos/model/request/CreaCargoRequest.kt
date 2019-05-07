package mx.univa.controldegastos.model.request

class CreaCargoRequest {

    var IdUsuario : Int = 0
        get()=field
        set(value){field = value}

    var IdDeuda : Int = 0
        get()=field
        set(value){field = value}

    var Cantidad : Double = 0.0
        get()=field
        set(value){field = value}
}