package com.lugares_v.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.lugares_v.model.Lugar


class LugarDao {

    private val coleccion1 ="lugaresApp"
private  val usuario= Firebase.auth.currentUser?.email.toString()
    private val coleccion2 ="misLugares"
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance() // conexion con la base de datos

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()//inicializar la conexion hacia firebase
    }

    fun getAllData() : MutableLiveData<List<Lugar>> {
     val listaLugares = MutableLiveData<List<Lugar>>()

        firestore.collection(coleccion1).document(usuario).collection(coleccion2)//va y busca los datos y regresa y los transforma intantanea
            .addSnapshotListener{instantanea, e-> //devuelve un arreglo de documentos
                 if(e !=null){ //se valida si se genero algun error en la captura de los datos
                     return@addSnapshotListener
                 }
                if(instantanea != null){ //si hay informacion recuperada
                //recorro la instantanea (documentos) para crear la lista de lugares
                val lista = ArrayList<Lugar>()
                    instantanea.documents.forEach{
//decirle para cada documento que voy hacer de esa instantanea
                        val lugar = it.toObject(Lugar::class.java)//toma un documento de firebase a un objeto de la clase lugar conversion de JSON a objeto
                    if (lugar!=null){lista.add(lugar)}
                    }
                    listaLugares.value=lista
                }

            }
    return listaLugares
    }

     fun saveLugar(lugar: Lugar)
     {
val documento: DocumentReference
if (lugar.id.isEmpty()){ //si id no tiene valor entonces es un documento nuevo
    documento =   firestore.collection(coleccion1).document(usuario).collection(coleccion2).document()
lugar.id=documento.id //se construyo el documento
}else{ //si el id tiene valor entonces el documento existe y recupero la info de el
    documento =   firestore.collection(coleccion1).document(usuario).
    collection(coleccion2).document(lugar.id)//ruta del firebase q yo ocupo


}
         documento.set(lugar) //se registra los datos de lugares en firebase
             .addOnSuccessListener { Log.d ("save lugar","se creo o modifico un lugar")}
             .addOnSuccessListener { Log.e ("save lugar","No se creo o modifico un lugar")}
     }

     fun deleteLugar(lugar: Lugar) {
         if(lugar.id.isNotEmpty()){ //si el id tiene valor entonces podemos eliminar el lugar
             firestore.collection(coleccion1).document(usuario).collection(coleccion2).
             document(lugar.id).delete()
                 .addOnSuccessListener { Log.d ("delete lugar","se elimino el lugar")}
                 .addOnSuccessListener { Log.e ("delete lugar","No se elimino el lugar")}

         }
     }

}