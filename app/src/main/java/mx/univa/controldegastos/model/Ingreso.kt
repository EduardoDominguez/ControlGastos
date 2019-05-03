package mx.univa.controldegastos.model

class Ingreso {
    var IdIngreso: Int = 0
        get()=field
        set(value){field = value}

    var Nombre : String = ""
        get()=field
        set(value){field = value}

    var IdUsuario: Int = 0
        get()=field
        set(value){field = value}

    var CantidadInicial: Double = 0.0
        get()=field
        set(value){field = value}

    var CantidadActual: Double = 0.0
        get()=field
        set(value){field = value}
}