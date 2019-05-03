package mx.univa.controldegastos.model.response

import mx.univa.controldegastos.model.Ingreso
import mx.univa.controldegastos.model.Respuesta

class ConsultaIngresosResponse: Respuesta() {

    var Ingresos: List<Ingreso> = ArrayList()
        get()=field
        set(value){field = value}
}