package mx.univa.controldegastos.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mx.univa.controldegastos.R
import mx.univa.controldegastos.model.Ingreso
import java.text.NumberFormat;
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorIngresos(ingresoList: List<Ingreso>, internal var context: Context): RecyclerView.Adapter<AdaptadorIngresos.IngresosViewHolder>() {

    internal var ingresoList: List<Ingreso> = ArrayList()
    init {
        this.ingresoList = ingresoList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorIngresos.IngresosViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.ingresos_item, parent, false)
        return IngresosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingresoList.size
    }

    override fun onBindViewHolder(holder: AdaptadorIngresos.IngresosViewHolder,
                                  position: Int) {
        val ingreso = ingresoList[position]
        holder.name.text = ingreso.Nombre

        var format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);

        holder.cantidad_actual.text = format.format(ingreso.CantidadActual).toString()
        holder.cantidad_inicial.text = format.format(ingreso.CantidadInicial).toString().toString()
        holder.itemView.setOnClickListener {
            /*val i = Intent(context, PersonFormActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", person.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)*/
        }
    }

    inner class IngresosViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name: TextView = view.findViewById(R.id.txv_name)
        var cantidad_inicial: TextView = view.findViewById(R.id.txv_cantidad_inicial)
        var cantidad_actual: TextView = view.findViewById(R.id.txv_cantidad_actual)
    }
}