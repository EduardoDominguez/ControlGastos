package mx.univa.controldegastos.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_fragment_ingresos_form.*
import mx.univa.controldegastos.PrincipalMain

import mx.univa.controldegastos.R
import mx.univa.controldegastos.adapter.AdaptadorIngresos
import mx.univa.controldegastos.model.Respuesta
import mx.univa.controldegastos.model.request.CreaIngresoRequest
import mx.univa.controldegastos.model.response.ConsultaIngresosResponse
import mx.univa.controldegastos.resourses.globales
import mx.univa.controldegastos.service.IngresosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.LocalDate
import java.time.ZoneId

import java.time.format.FormatStyle
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentIngresosForm.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentIngresosForm.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentIngresosForm : Fragment() {

    var txtFecha : TextView? = null
    var btnRegistrar : Button? = null
    lateinit var service: IngresosService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(globales.URL_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<IngresosService>(IngresosService::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista : View = inflater.inflate(R.layout.fragment_fragment_ingresos_form, container, false)
        txtFecha = vista.findViewById(R.id.edt_fecha_ingreso) as? TextView
        btnRegistrar = vista.findViewById(R.id.btn_registrar_ingreso) as? Button


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        txtFecha?.setOnClickListener({
            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, dayOfMonth ->
                //txtFecha?.text = "${mYear}-${mMonth+1}-${dayOfMonth}"

                var strMes : String= (mMonth+1).toString() //(mMonth+1)..toString().length == 1)?"0"+(mMonth+1): (mMonth+1)
                if(strMes.length == 1)
                    strMes = "0"+strMes

                var strDia : String= dayOfMonth.toString() //(mMonth+1)..toString().length == 1)?"0"+(mMonth+1): (mMonth+1)
                if(strDia.length == 1){
                    strDia = "0"+strDia
                }

                txtFecha?.text = "${mYear}-${strMes}-${strDia}"
            }, year,month,day)
            dpd.show()
        })

        btnRegistrar?.setOnClickListener({
            GuardaIngreso()
        })

        return vista
    }

    fun GuardaIngreso(){
        var actividad = activity as? PrincipalMain
        var request = CreaIngresoRequest()
        request.IdUsuario = actividad!!.obtieneIdUsuarioSesion() //Obtener Id Usuario
        request.Nombre = edt_nombre?.text.toString()
        request.Cantidad = edt_cantidad?.text.toString().toDouble()
        request.Fecha = txtFecha?.text.toString()

        var progress = progressAddIngresos
        progress.visibility = View.VISIBLE
        service.creaIngreso(request).enqueue(object: Callback<Respuesta> {

            override fun onResponse(call: Call<Respuesta>?, response: Response<Respuesta>?) {
                val respuesta = response?.body()
                //Log.i(globales.TAG_LOGS, Gson().toJson(respuesta))

                if(respuesta!!.isExito()){
                    var actividad = activity as? PrincipalMain
                    actividad?.CargarFragment(FragmentIngresos())
                }
                Toast.makeText(activity, respuesta.getMensaje(), Toast.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
            override fun onFailure(call: Call<Respuesta>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "No se pudo comunicar con el servicio, favor de intentarlo más tarde", Toast.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
        })
    }

}
