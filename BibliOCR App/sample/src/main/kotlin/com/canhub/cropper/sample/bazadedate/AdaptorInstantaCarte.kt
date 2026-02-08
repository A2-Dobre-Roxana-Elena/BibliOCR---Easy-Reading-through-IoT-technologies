//package com.canhub.cropper.sample.bazadedate

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.InstantaCarte
import com.example.croppersample.R

class AdaptorInstantaCarte(
  private val instantaList: List<InstantaCarte>,
  private val isAdmin: Boolean,
  private val onImprumutaClick: (InstantaCarte) -> Unit
) : RecyclerView.Adapter<AdaptorInstantaCarte.InstantaViewHolder>() {

  private lateinit var bazaDeDate: BazaDeDate

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstantaViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instanta_carte, parent, false)
    return InstantaViewHolder(view)
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: InstantaViewHolder, position: Int) {
    val instanta = instantaList[position]
    holder.idInstantaTextView.text = "Id Instanță: ${instanta.id}"
    holder.edituraTextView.text = "Editură: ${instanta.editura}"
    holder.isbnTextView.text = "ISBN: ${instanta.isbn}"
    holder.anTextView.text = "Anul publicării: ${instanta.anCarte}"
    if(!instanta.eimprumatata)
      holder.disponibilitate.text = "Exemplarul e disponibil"
    else
      holder.disponibilitate.text = "Exemplarul e împrumutat"

    bazaDeDate = BazaDeDate()

    if (isAdmin) {
      if(!instanta.eimprumatata)
      {
        holder.imprumutaButton.visibility = View.VISIBLE
        holder.returneazaButton.visibility = View.GONE

        holder.imprumutaButton.setOnClickListener {
          onImprumutaClick(instanta)

          bazaDeDate.imprumutaCarte(instanta.id) { success ->
          if (success) {
            println("Cartea a fost împrumutată cu succes.")
            } else {
            println("Eroare la împrumutarea cărții.")
            }
          }
          Handler(Looper.getMainLooper()).postDelayed({
            holder.imprumutaButton.visibility = View.GONE
            holder.returneazaButton.visibility = View.VISIBLE
            holder.disponibilitate.text = "Exemplarul e împrumutat"
          }, 3000)

        }

      }
      else
      {
        holder.imprumutaButton.visibility = View.GONE
        holder.returneazaButton.visibility = View.VISIBLE

        holder.returneazaButton.setOnClickListener {
          bazaDeDate.returneazaCarte(instanta.id) { success ->
          if (success) {
            println("Cartea a fost împrumutată cu succes.")
          } else {
            println("Eroare la împrumutarea cărții.")
          }
        }
          Handler(Looper.getMainLooper()).postDelayed({
            holder.imprumutaButton.visibility = View.VISIBLE
            holder.returneazaButton.visibility = View.GONE
            holder.disponibilitate.text = "Exemplarul e disponibil"
          }, 1000)

        }

      }

    } else {
      holder.imprumutaButton.visibility = View.GONE
      holder.returneazaButton.visibility = View.GONE
    }

  }

  override fun getItemCount(): Int {
    return instantaList.size
  }

  inner class InstantaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val idInstantaTextView: TextView = itemView.findViewById(R.id.text_view_id_instanta)
    val edituraTextView: TextView = itemView.findViewById(R.id.text_view_editura)
    val isbnTextView: TextView = itemView.findViewById(R.id.text_view_isbn)
    val anTextView: TextView = itemView.findViewById(R.id.text_view_an)
    val disponibilitate: TextView = itemView.findViewById(R.id.text_view_disponibilitate)
    val imprumutaButton: Button = itemView.findViewById(R.id.btn_imprumuta)
    val returneazaButton: Button = itemView.findViewById(R.id.btn_returneaza)
  }
}


//
//class AdaptorInstantaCarte(
//  private val instantaList: List<InstantaCarte>,
//  private val isAdmin: Boolean,
//  private val onImprumutaClick: (InstantaCarte) -> Unit
//) : RecyclerView.Adapter<AdaptorInstantaCarte.InstantaViewHolder>() {
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstantaViewHolder {
//    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instanta_carte, parent, false)
//    return InstantaViewHolder(view)
//  }
//
//  @SuppressLint("SetTextI18n")
//  override fun onBindViewHolder(holder: InstantaViewHolder, position: Int) {
//    val instanta = instantaList[position]
//    holder.idInstantaTextView.text = "Id Instanta: ${instanta.id}"
//    holder.edituraTextView.text = "Editura: ${instanta.editura}"
//    holder.isbnTextView.text = "ISBN: ${instanta.isbn}"
//
//    if (isAdmin) {
//      holder.imprumutaButton.visibility = View.VISIBLE
//      holder.imprumutaButton.setOnClickListener {
//        onImprumutaClick(instanta)
//      }
//    } else {
//      holder.imprumutaButton.visibility = View.GONE
//    }
//  }
//
//  override fun getItemCount(): Int {
//    return instantaList.size
//  }
//
//  inner class InstantaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val idInstantaTextView: TextView = itemView.findViewById(R.id.text_view_id_instanta)
//    val edituraTextView: TextView = itemView.findViewById(R.id.text_view_editura)
//    val isbnTextView: TextView = itemView.findViewById(R.id.text_view_isbn)
//    val imprumutaButton: Button = itemView.findViewById(R.id.btn_imprumuta)
//  }
//}

//class AdaptorInstantaCarte(
//  private val instantaList: List<InstantaCarte>
//) : RecyclerView.Adapter<AdaptorInstantaCarte.InstantaViewHolder>() {
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstantaViewHolder {
//    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instanta_carte, parent, false)
//    return InstantaViewHolder(view)
//  }
//
//  @SuppressLint("SetTextI18n")
//  override fun onBindViewHolder(holder: InstantaViewHolder, position: Int) {
//    val instanta = instantaList[position]
//    holder.idInstantaTextView.text = "Id Instanta: ${instanta.id}"
//    holder.edituraTextView.text = "Editura: ${instanta.editura}"
//    holder.isbnTextView.text = "ISBN: ${instanta.isbn}"
//  }
//
//  override fun getItemCount(): Int {
//    return instantaList.size
//  }
//
//  inner class InstantaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val idInstantaTextView: TextView = itemView.findViewById(R.id.text_view_id_instanta)
//    val edituraTextView: TextView = itemView.findViewById(R.id.text_view_editura)
//    val isbnTextView: TextView = itemView.findViewById(R.id.text_view_isbn)
//  }
//}
//
//
//// class AdaptorInstantaCarte(private val instantaList: List<InstantaCarte>) : RecyclerView.Adapter<AdaptorInstantaCarte.InstantaViewHolder>() {
////
////  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstantaViewHolder {
////    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instanta_carte, parent, false)
////    return InstantaViewHolder(view)
////  }
////
////  @SuppressLint("SetTextI18n")
////  override fun onBindViewHolder(holder: InstantaViewHolder, position: Int) {
////    val instanta = instantaList[position]
////    holder.idInstantaTextView.text = "Id Instanta: ${instanta.id}"
////    holder.edituraTextView.text = "Editura: ${instanta.editura}"
////    holder.isbnTextView.text = "ISBN: ${instanta.isbn}"
////  }
////
////  override fun getItemCount(): Int {
////    return instantaList.size
////  }
////
////  inner class InstantaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////    val idInstantaTextView: TextView = itemView.findViewById(R.id.text_view_id_instanta)
////    val edituraTextView: TextView = itemView.findViewById(R.id.text_view_editura)
////    val isbnTextView: TextView = itemView.findViewById(R.id.text_view_isbn)
////  }
////}
////
//////package com.canhub.cropper.sample.bazadedate
//////
//////import android.view.LayoutInflater
//////import android.view.View
//////import android.view.ViewGroup
//////import android.widget.TextView
//////import androidx.recyclerview.widget.RecyclerView
//////import com.example.croppersample.R
//////
//////class AdaptorInstantaCarte(private val instantaCarteList: MutableList<InstantaCarte>) :
//////  RecyclerView.Adapter<AdaptorInstantaCarte.InstantaCarteViewHolder>() {
//////
//////  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstantaCarteViewHolder {
//////    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instanta_carte, parent, false)
//////    return InstantaCarteViewHolder(view)
//////  }
//////
//////  override fun onBindViewHolder(holder: InstantaCarteViewHolder, position: Int) {
//////    val instantaCarte = instantaCarteList[position]
//////    holder.edituraTextView.text = instantaCarte.editura
////////    holder.anulPublicariiTextView.text = instantaCarte.anulPublicarii.toString()
//////  }
//////
//////  override fun getItemCount(): Int {
//////    return instantaCarteList.size
//////  }
//////
//////  fun updateData(newInstantaCarteList: List<InstantaCarte>) {
//////    // Calculăm diferențele dintre lista veche și cea nouă pentru a folosi notificări mai precise
//////    val oldSize = instantaCarteList.size
//////    val newSize = newInstantaCarteList.size
//////
//////    instantaCarteList.clear()
//////    notifyItemRangeRemoved(0, oldSize)
//////
//////    instantaCarteList.addAll(newInstantaCarteList)
//////    notifyItemRangeInserted(0, newSize)
//////  }
//////
//////  class InstantaCarteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//////    val edituraTextView: TextView = itemView.findViewById(R.id.text_view_editura)
////////    val anulPublicariiTextView: TextView = itemView.findViewById(R.id.text_view_anul_publicarii)
//////  }
//////}
