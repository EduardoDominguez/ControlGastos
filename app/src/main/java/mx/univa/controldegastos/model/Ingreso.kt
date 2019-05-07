package mx.univa.controldegastos.model

class Ingreso {

    var idingreso: Int = 0
        get()=field
        set(value){field = value}

    var nombre : String = ""
        get()=field
        set(value){field = value}

    var IdUsuario: Int = 0
        get()=field
        set(value){field = value}

    var cantidad_inicial: Double = 0.0
        get()=field
        set(value){field = value}

    var cantidad_actual: Double = 0.0
        get()=field
        set(value){field = value}

    override fun toString(): String = this.nombre
}