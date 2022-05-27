package com.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //aqui se ponen los atributos
//**************************************/
    //// inicializacion tardia, var(normal variable global), nombre variable que es auth, rojo es tipo de variables vamos a usar
    private lateinit var auth: FirebaseAuth //tenemos acceso a la autenticacion firebase

    //                   binding habilitar las vistas o generar
    private lateinit var binding: ActivityMainBinding //acceso a nuestra clave y pantalla de nuestra aplicacion

    //ActivityMainBinding es el mismo de activity_main.xml
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//Se establece el enlace con la vista xml mediante el objeto binding
        binding =
            ActivityMainBinding.inflate(layoutInflater) //tener acceso a los elementos en la aplicacion
        setContentView(binding.root)
        //Recorte 005: Activar la autenticación

//Se inicializa Firebase y se asigna el objeto para autenticación
        FirebaseApp.initializeApp(this) //para tene acceso a internet o firebase y no este caido el servicio y demas cosas
        auth = Firebase.auth //inicializamos la variable de la linea 16 para hacer autenticacion
        // Completado la autenticacion
        //****************************************//

        // cuando le de click a ese boton
        binding.btRegister.setOnClickListener { haceRegistro() }
        binding.btLogin.setOnClickListener { haceLogin() }


    }

    // Recorte 008: haceRegistro

    private fun haceRegistro() {
        //crea dos variables
        val email = binding.etCorreo.text.toString()//extrae lo q puso la persona
        val clave = binding.etClave.text.toString()//extrae lo q puso la persona la clave

        //Se usa la función para crear un usuario por medio de correo y contraseña
        auth.createUserWithEmailAndPassword(
            email,
            clave
        )//invocamos la variable de autenticacion de la linea 16 y llama a los metodos de email y clave
            .addOnCompleteListener(this) { task -> //la tarea se logro
                if (task.isSuccessful) {   //fue creado correctamente
                    val user = auth.currentUser
                    actualiza(user)
                } else { //sino
                    Toast.makeText(
                        baseContext,  //manda un mensaje que fallo el registro
                        getString(R.string.msg_fallo_registro),
                        Toast.LENGTH_SHORT
                    ).show()
                    actualiza(null)
                }
            }
    }

    //Recorte 007: Método haceLogin

    private fun haceLogin() {
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Se usa la función para crear un usuario por medio de correo y contraseña el mismo proceso
        auth.signInWithEmailAndPassword(email, clave)//metodo se llama autentiquese
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else { //sino
                    Toast.makeText(
                        baseContext, //si el usuario no exite o cleve incorrecta
                        getString(R.string.msg_fallo_login), //manda mensaje de errorr
                        Toast.LENGTH_SHORT
                    ).show()
                    actualiza(null)
                }
            }
    }

//Recorte 009: actualiza

    private fun actualiza(user: FirebaseUser?) { //en el parametro dice recibe un posible auntenticado o nulo que no se pudo
        if (user != null) { //el usuario existe?
            // paso a la pantalla principal
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }
    }

   // Recorte 010; onStart()

    public override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        actualiza(user)
    }



}