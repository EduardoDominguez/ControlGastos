package mx.univa.controldegastos.model.response

import mx.univa.controldegastos.model.Respuesta

class TotalIngresosResponse: Respuesta() {

    var Total: Double = 0.0
        get()=field
        set(value){field = value}
}