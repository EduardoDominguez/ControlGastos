package mx.univa.controldegastos.model

open class Respuesta {
    private var Mensaje: String
    private var Exito: Boolean
    private var CodigoError : String

    init{
        this.CodigoError = ""
        this.Exito = false
        this.Mensaje = ""
    }

    fun getMensaje():String{
        return this.Mensaje
    }

    fun isExito():Boolean{
        return this.Exito
    }

    fun getCodigoError():String{
        return this.CodigoError
    }
}