package mx.univa.controldegastos.model.response

import mx.univa.controldegastos.model.Deuda
import mx.univa.controldegastos.model.Respuesta

class ConsultaDeudasResponse : Respuesta(){
    var Deudas: List<Deuda> = ArrayList()
        get()=field
        set(value){field = value}

}