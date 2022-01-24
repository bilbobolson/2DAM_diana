package com.pmdm.diana

/**
 * Antonio José Sánchez Bujaldón
 * Programación de Aplicaciones Multimedia y de Dispositivos Móviles
 * curso 2021|22
 */

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.google.android.material.snackbar.Snackbar
import com.pmdm.diana.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityInfoBinding
    private lateinit var sp: SharedPreferences

    /**
     * @param savedInstanceState: Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // abrimos el archivo de preferencias compartidas
        sp = getSharedPreferences(getString(R.string.file_prefs), MODE_PRIVATE)

        binding.apply {

            // mostramos el valor del umbral en el cuadro de texto
            infoUmbral.setText(sp.getInt("umbral",10).toString())

            // guardamos el valor del umbral
            infoBotonUmbral.setOnClickListener {

                // comprobación de errores
                if (infoUmbral.text?.isNotEmpty() == true)
                {
                    // recuperamos el valor del umbral
                    val umbral = infoUmbral.text.toString().toInt()

                    if (umbral >= 0)
                    {
                        sp.edit {
                            putInt("umbral", umbral)
                            //apply()
                            commit()
                        }

                        finish()
                        return@setOnClickListener
                    }
                }

                // Mostramos mensaje de error
                Snackbar.make(root,R.string.msg_error_umbral,Snackbar.LENGTH_LONG).show()
            }
        }
    }
}