package com.canhub.cropper.sample

import AdaptorInstantaCarte
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.InstantaCarte
import com.example.croppersample.R
import timber.log.Timber

class PaginaCarteActivity : AppCompatActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var instantaCarteAdapter: AdaptorInstantaCarte
  private lateinit var instantaCarteList: MutableList<InstantaCarte>
  private lateinit var bazaDeDate: BazaDeDate
  private lateinit var titluCarteTextView: TextView
  private lateinit var autorCarteTextView: TextView
  private lateinit var idTextView: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pagina_carte)

    idTextView = findViewById(R.id.text_view_id_carte)
    titluCarteTextView = findViewById(R.id.text_view_titlu_carte)
    autorCarteTextView = findViewById(R.id.text_view_autor_carte)
    recyclerView = findViewById(R.id.recycler_view_instance_carte)
    recyclerView.layoutManager = LinearLayoutManager(this)

    instantaCarteList = mutableListOf()
    val isAdmin = intent.getBooleanExtra("isAdmin", false)

    instantaCarteAdapter = AdaptorInstantaCarte(instantaCarteList, isAdmin) { instanta ->
      val intent = Intent(this, PaginaImprumuturi::class.java)
      intent.putExtra("idInstanta", instanta.id)
      startActivity(intent)
    }
    recyclerView.adapter = instantaCarteAdapter

    bazaDeDate = BazaDeDate()

    val idCarte = intent.getStringExtra("idCarte")

    fetchCarteDetailsFromDatabase(idCarte!!, { titlu, autor, id ->
      titluCarteTextView.text = titlu
      autorCarteTextView.text = autor
      idTextView.text = id
    }, { e ->
      Timber.e("Error fetching carte details: $e")
    })

    fetchInstantaCarteFromDatabase(idCarte)
  }

  private fun fetchCarteDetailsFromDatabase(idCarte: String, onSuccess: (String, String, String) -> Unit, onFailure: (Exception) -> Unit) {
    bazaDeDate.fetchCarteTitlu(idCarte, { carte ->
      onSuccess(carte.titlu, carte.autor, carte.id)
    }, { e ->
      onFailure(e)
    })
  }

  private fun fetchInstantaCarteFromDatabase(idCarte: String) {
    bazaDeDate.fetchInstantaCarte(idCarte, { instante ->
      val oldSize = instantaCarteList.size
      instantaCarteList.clear()
      instantaCarteAdapter.notifyItemRangeRemoved(0, oldSize)

      instantaCarteList.addAll(instante)
      instantaCarteAdapter.notifyItemRangeInserted(0, instante.size)
    }, { e ->
      Timber.e("Error fetching instanta carte: $e")
    })
  }
}
