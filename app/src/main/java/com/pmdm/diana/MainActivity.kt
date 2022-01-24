package com.pmdm.diana

/**
 * Antonio José Sánchez Bujaldón
 * Programación de Aplicaciones Multimedia y de Dispositivos Móviles
 * curso 2021|22
 */

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pmdm.diana.databinding.ActivityMainBinding
import java.lang.Math.abs
import kotlin.properties.Delegates.observable
import kotlin.random.Random

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    private var umbral: Int = 0
    private var puntuacion: Int by observable(0) {
            _,_,newValue -> binding.juegoMarcador.text = getString(R.string.msg_marcador, newValue)
    }

    private var tiradas:Int by observable(5) {
            _,_,newValue -> binding.juegoTiradas.text = getString(R.string.msg_tiradas, newValue)
    }

    private var numero: Int by observable(0) {
            _,_,newValue -> binding.juegoMensaje.text  = getString(R.string.msg_juego, newValue)
    }

    /**
     * @param savedInstanceState: Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //supportActionBar?.hide()

        resetGame()

        binding.apply {

            // Ventana de información
            juegoBtnInfo.setOnClickListener {
                val intencion = Intent(this@MainActivity, InfoActivity::class.java)
                startActivity(intencion)
                //Snackbar.make(it, R.string.msg_info, Snackbar.LENGTH_LONG).show()
            }

            // Reseteo del juego
            juegoBtnReset.setOnClickListener { resetGame() }


            // Disparamos!!!
            juegoBtnDisparar.setOnClickListener {

                var puntos = 0

                // emitimos sonido de lanzamiento
                val mp = MediaPlayer.create(this@MainActivity, R.raw.throw_dart)
                    mp.start()

                // gastamos un dardo
                tiradas--

                // recuperamos el valor del slider
                val valor = binding.juegoBarra.value.toInt()

                // comparamos con el número que se obtuvo de forma aleatoria
                // y calculamos los puntos de la tirada
                if (valor==numero) puntos = 100
                else if (dentroDeUmbral(valor)) puntos = 100 - (10*(5-tiradas))

                // actualizamos la puntuación total
                puntuacion += puntos

                mp.setOnCompletionListener {

                    var madb = MaterialAlertDialogBuilder(this@MainActivity)
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton(R.string.btn_continuar) { i, w ->
                                        numero = Random.nextInt(1,100)
                                        /*actualizarMarcador()*/
                                    }

                    if (valor==numero)
                        madb.setMessage(getString(R.string.msg_info_acierto,  puntos))
                            .setNegativeButton(R.string.btn_terminar) { i, w -> resetGame() }
                    else
                        madb.setMessage(getString(R.string.msg_info_tirada, valor, puntos))
                    //
                    madb.show()
                }
            }
        }
    }

    /**
     */
    override fun onStart()
    {
        super.onStart()

        // Abrimos el archivo de preferencias compartidas y leemos el valor del umbral
        // IMPORTANTE: MODE_PRIVATE es el único modo disponible a partir de la API 24
        val sp = getSharedPreferences(getString(R.string.file_prefs), MODE_PRIVATE)
        umbral = sp.getInt("umbral", 10)
    }

    /**
     * Resetea las condiciones del juego
     */
    private fun resetGame()
    {
        binding.apply {

            // iniciamos la barra a la mitad
            juegoBarra.value = 50f

            // reiniciamos las variables
            tiradas = 5
            puntuacion = 0

            // elegimos valor aleatorio
            numero = Random.nextInt(1,100)

            // actualizamos los marcadores
            /*actualizarMarcador()*/
        }
    }

    /**
     * Comprueba si el valor dado está dentro del umbral
     * @param valor: Int
     * @return
     */
    private fun dentroDeUmbral(valor: Int):Boolean
    {
        // calculamos el valor absoluto
        val absoluto = abs(numero-valor)

        // comprobamos si estamos dentro del rango [1,umbral]
        return ((absoluto >= 1) && (absoluto <= umbral))
    }

    /*private fun actualizarMarcador()
    {
        binding.apply {
            // mostramos los marcadores
            juegoMarcador.text = getString(R.string.msg_marcador, puntuacion)
            juegoTiradas.text = getString(R.string.msg_tiradas, tiradas)
            juegoMensaje.text  = getString(R.string.msg_juego, numero)
        }
    }*/
}








