package mx.univa.controldegastos.model.request

class CreaDeudaRequest {

    var Nombre : String = ""
        get()=field
        set(value){field = value}

    var DiaCorte : Int = 0
        get()=field
        set(value){field = value}

    var DiaLimitePago : Int = 0
        get()=field
        set(value){field = value}

    var IdUsuario : Int = 0
        get()=field
        set(value){field = value}
}