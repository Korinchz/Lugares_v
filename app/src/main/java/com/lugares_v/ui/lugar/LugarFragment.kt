package com.lugares_v.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lugares_v.R
import com.lugares_v.adapter.LugarAdapter
import com.lugares_v.databinding.FragmentLugarBinding
import com.lugares_v.viewmodel.LugarViewModel

class LugarFragment : Fragment() {
    private var _binding: FragmentLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: LugarViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)
        _binding = FragmentLugarBinding.inflate(inflater, container, false)
        binding.addLugar.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }///ojo me sirve doble casa y ver mas

     //Activar el reciclador
        val lugarAdapter = LugarAdapter()
        val reciclador = binding.reciclador //tener en enlace con el dibujo de fragment lugar
reciclador.adapter = lugarAdapter //adaptando el reciclador al lugar adaptador

        reciclador.layoutManager = LinearLayoutManager(requireContext())//es para pintar sobre el lienzo de forma ordena con vista de cuadricula
      lugarViewModel.getAllData.observe(viewLifecycleOwner){
          lugarAdapter.setData(it)
      }                                    //getALLdata=inspeccionar si algo se ha cambiado o modifica datos vivo como traduccion
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}