package com.lugares_v.ui.lugar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares_v.R
import com.lugares_v.databinding.FragmentUpdateLugarBinding
import com.lugares_v.model.Lugar
import com.lugares_v.viewmodel.LugarViewModel


class UpdateLugarFragment : Fragment() {

    //se decriben los parametros por argumentos
    private  val args by navArgs<UpdateLugarFragmentArgs>()


    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: LugarViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //Coloco la info del lugaar en los campos del fragmento para modificar
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)
        binding.tvAltura.setText(args.lugar.altura.toString())
        binding.tvLatitud.setText(args.lugar.latitud.toString())
        binding.tvLongitud.setText(args.lugar.longitud.toString())


        binding.btUpdateLugar.setOnClickListener { updateLugar()}
        binding.btEmail.setOnClickListener{escribirCorreo()}
        binding.btPhone.setOnClickListener{ realizarllamada() }
        binding.btWeb.setOnClickListener{ verweb() }
        // se indica que esta pantalla tiene un menu personalizado...
        setHasOptionsMenu(true) // esta pantalla tiene opciones de menu

        return binding.root
    }

    private fun verweb() {
        val recurso = binding.etWeb.text.toString()
        if(recurso.isNotEmpty()){

            val accion = Intent(Intent.ACTION_VIEW, Uri.parse("http://$recurso"))
            startActivity(accion)

        } else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_SHORT).show()
        }


    }

    private fun realizarllamada() {
        val recurso = binding.etTelefono.text.toString()
        if(recurso.isNotEmpty()){
            val accion = Intent(Intent.ACTION_CALL)
            accion.data = Uri.parse("tel:$recurso")
        if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)!=
                PackageManager.PERMISSION_GRANTED){
            //si no se ha otrogado el permiso de hacer llamadas... se pide el permiso
            requireActivity()
                .requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
        }else{ //si se tiene permiso se hace llamada
            requireActivity().startActivity(accion)
        }
        } else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_SHORT).show()
        }

    }

    private fun escribirCorreo() {
    val recurso = binding.etCorreo.text.toString()
    if(recurso.isNotEmpty()){
        val accion = Intent(Intent.ACTION_SEND)
        accion.type="message/rfc822"
        accion.putExtra(Intent.EXTRA_EMAIL, arrayOf(recurso))
        accion.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.msg_saludos)+" "+binding.etNombre.text)
        accion.putExtra(Intent.EXTRA_TEXT,getString(R.string.msg_mensaje_correo))
        startActivity(accion)//efectivamente se carga el app de correo
    } else {
        Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_SHORT).show()
    }

}

    //esto para anadir en todas la vista que se ocupe la opcion del basurero o borrar osea los iconos que quiera
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { //inflater = es un inflador
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // a que le de click al delete_menu o cualquier boton queramos programar
       //Consulto si se le dio click en el icono de borrar
        if(item.itemId==R.id.menu_delete){
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteLugar() {
        val pantalla=AlertDialog.Builder(requireContext())

        pantalla.setTitle(R.string.delete)
        pantalla.setMessage(getString(R.string.seguroBorrar)+"${args.lugar.nombre}?")// ${args.lugar.nombre}" esto para espeficar el nombre de las tarjeticas q salga nombre q desee borrar

        pantalla.setPositiveButton(getString(R.string.si)){ _,_->

            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }

        pantalla.setNegativeButton(getString(R.string.no)){ _,_-> }
        pantalla.create().show()
    }

    //por aca estamos por el momento...
    private fun updateLugar() {
        val nombre=binding.etNombre.text.toString()
        val correo=binding.etCorreo.text.toString()
        val telefono=binding.etTelefono.text.toString()
        val web=binding.etWeb.text.toString()
        if (nombre.isNotEmpty()) { //Si puedo crear un lugar
            val lugar= Lugar(args.lugar.id,nombre,correo,telefono,web,0.0,
                0.0,0.0,"","")

            lugarViewModel.saveLugar(lugar)

            Toast.makeText(requireContext(),getString(R.string.msg_lugar_update),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        } else {  //Mensaje de error...
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}