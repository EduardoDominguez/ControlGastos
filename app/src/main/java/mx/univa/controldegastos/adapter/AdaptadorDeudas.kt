package mx.univa.controldegastos.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mx.univa.controldegastos.R
import mx.univa.controldegastos.model.Deuda
import java.text.NumberFormat;
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorDeudas(deudaList: List<Deuda>, internal var context: Context): RecyclerView.Adapter<AdaptadorDeudas.DeudasViewHolder>() {

    internal var deudaList: List<Deuda> = ArrayList()
    init {
        this.deudaList = deudaList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorDeudas.DeudasViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.deudas_item, parent, false)
        return DeudasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deudaList.size
    }

    override fun onBindViewHolder(holder: AdaptadorDeudas.DeudasViewHolder,
                                  position: Int) {
        val deuda = deudaList[position]
        holder.name.text = deuda.nombre


        holder.dia_corte.text = deuda.dia_corte.toString()
        holder.dia_limite.text = deuda.dia_limite_pago.toString()
        holder.itemView.setOnClickListener {
            /*val i = Intent(context, PersonFormActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", person.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)*/
        }
    }

    inner class DeudasViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name: TextView = view.findViewById(R.id.txv_name)
        var dia_corte: TextView = view.findViewById(R.id.txv_dia_corte)
        var dia_limite: TextView = view.findViewById(R.id.txv_dia_limite)
    }
}