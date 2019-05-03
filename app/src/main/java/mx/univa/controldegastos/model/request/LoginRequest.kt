package mx.univa.controldegastos.model.request

class LoginRequest {
    private var Username:String
    private var Password: String

    init{
        this.Username = ""
        this.Password = ""
    }

    fun setUsername(pUsername: String){
        this.Username = pUsername
    }

    fun setPassword(pPassword: String ){
        this.Password = pPassword
    }
}