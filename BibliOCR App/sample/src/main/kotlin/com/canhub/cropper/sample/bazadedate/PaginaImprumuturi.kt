package com.canhub.cropper.sample

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.Imprumut
import com.example.croppersample.R
import timber.log.Timber

class PaginaImprumuturi : AppCompatActivity() {

  private lateinit var bazaDeDate: BazaDeDate
  private lateinit var idInstanta: String
  private lateinit var editTextUser: EditText
  private lateinit var buttonImprumuta: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pagina_imprumut)

    idInstanta = intent.getStringExtra("idInstanta")!!

    editTextUser = findViewById(R.id.edit_text_user)
    buttonImprumuta = findViewById(R.id.button_imprumuta)

    bazaDeDate = BazaDeDate()

    buttonImprumuta.setOnClickListener {
      val user = editTextUser.text.toString()
      val imprumut = Imprumut(
        id = "0",
        idInstantaCarte = idInstanta,
        idUser = user,
        dataImprumutInput = "12.21.2332",
        dataRestituireInput = "cd")

      bazaDeDate.addImprumut(imprumut, {
        Timber.i("Imprumut adăugat cu succes!")
        finish()
      }, { e ->
        Timber.e("Eroare la adăugarea împrumutului: $e")
      })
    }
  }
}
