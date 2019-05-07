package mx.univa.controldegastos.model.request

import java.time.LocalDate
import java.util.*

class CreaIngresoRequest {

    var Nombre : String = ""
        get()=field
        set(value){field = value}

    var Fecha : String = ""
        get()=field
        set(value){field = value}

    /*var Fecha : Date = Date()
        get()=field
        set(value){field = value}*/

    var Cantidad : Double = 0.0
        get()=field
        set(value){field = value}

    var IdUsuario : Int = 0
        get()=field
        set(value){field = value}
}