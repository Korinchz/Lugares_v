package com.lugares_v.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares_v.databinding.LugarFilaBinding
import com.lugares_v.model.Lugar
import com.lugares_v.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() { //es unas clase interna de LugarViewHolder el doble punto es heredar osea hereda RecyclerView.ViewHolder
    //una lista para gestionar la información de los lugares
    private var lista = emptyList<Lugar>()
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder (itemBinding.root){
        fun dibuja(lugar: Lugar) {
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvWeb.text = lugar.web

            //Mostrar la imagen del lugar en el card
            Glide.with(itemBinding.root.context) //alojar en la memoria
                .load(lugar.rutaImagen)//cargar la imagen
                .circleCrop()
                .into(itemBinding.imagen) //pongalo la imagen en el card para dibujarlo

            //Activa el click para modificar un lugar
            itemBinding.vistaFila.setOnClickListener {
                val accion = LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(accion)
            }
        }
    }
    //Acá se va a crear una "cajita" del reciclador...  una fila... sólo la estructura
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding =
            LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
                parent,false)
        return LugarViewHolder(itemBinding)
    }
    //Acá se va a solicitar "dibujar" una cajita, según el elemento de la lista...
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = lista[position]
        holder.dibuja(lugar)
    }
    override fun getItemCount(): Int {
        return lista.size
    }

    fun setData(lugares: List<Lugar>) { //reudibujar lo que hay en reciclador de la cajita
        lista = lugares
        notifyDataSetChanged()
    }

}
