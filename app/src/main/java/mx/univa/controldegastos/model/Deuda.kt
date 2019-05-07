package mx.univa.controldegastos.model

class Deuda {


    var iddeuda: Int = 0
        get()=field
        set(value){field = value}

    var estatus : Int = 0
        get()=field
        set(value){field = value}


    var dia_corte : Int = 0
        get()=field
        set(value){field = value}


    var dia_limite_pago : Int = 0
        get()=field
        set(value){field = value}


    var idusuario : Int = 0
        get()=field
        set(value){field = value}

    var nombre : String = ""
        get()=field
        set(value){field = value}


    override fun toString(): String = this.nombre
}