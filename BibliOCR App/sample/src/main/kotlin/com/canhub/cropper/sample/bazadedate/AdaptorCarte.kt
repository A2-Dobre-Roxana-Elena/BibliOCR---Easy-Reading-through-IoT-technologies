package com.canhub.cropper.sample.bazadedate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.croppersample.R

class AdaptorCarte(
  private val carteList: List<Carte>,
  private val onItemClick: (Carte) -> Unit
) : RecyclerView.Adapter<AdaptorCarte.CarteViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarteViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carte, parent, false)
    return CarteViewHolder(view)
  }

  override fun onBindViewHolder(holder: CarteViewHolder, position: Int) {
    val carte = carteList[position]
    holder.titluTextView.text = carte.titlu
    holder.autorTextView.text = carte.autor
    holder.idTextView.text = carte.id

    holder.itemView.setOnClickListener {
      onItemClick(carte)
    }
  }

  override fun getItemCount(): Int {
    return carteList.size
  }

  inner class CarteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titluTextView: TextView = itemView.findViewById(R.id.text_view_title)
    val autorTextView: TextView = itemView.findViewById(R.id.text_view_author)
    val idTextView:TextView = itemView.findViewById(R.id.text_view_id);
  }
}

//package com.canhub.cropper.sample.bazadedate
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.croppersample.R
//
//class AdaptorCarte(
//  private val carteList: List<Carte>,
//  private val onItemClick: (Carte) -> Unit
//) : RecyclerView.Adapter<AdaptorCarte.CarteViewHolder>() {
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarteViewHolder {
//    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carte, parent, false)
//    return CarteViewHolder(view)
//  }
//
//  override fun onBindViewHolder(holder: CarteViewHolder, position: Int) {
//    val carte = carteList[position]
//    holder.titluTextView.text = carte.titlu
//    holder.autorTextView.text = carte.autor
//
//    holder.itemView.setOnClickListener {
//      onItemClick(carte)
//    }
//  }
//
//  override fun getItemCount(): Int {
//    return carteList.size
//  }
//
//  inner class CarteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val titluTextView: TextView = itemView.findViewById(R.id.text_view_title)
//    val autorTextView: TextView = itemView.findViewById(R.id.text_view_author)
//  }
//}
//
//
////
////class AdaptorCarte(private val carteList: MutableList<Carte>) : RecyclerView.Adapter<AdaptorCarte.CarteViewHolder>() {
////
////  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarteViewHolder {
////    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carte, parent, false)
////    return CarteViewHolder(view)
////  }
////
////  override fun onBindViewHolder(holder: CarteViewHolder, position: Int) {
////    val carte = carteList[position]
////    holder.idCarteTextView.text = carte.id
////    holder.titluTextView.text = carte.titlu
////    holder.autorTextView.text = carte.autor
////
////    holder.viewDetailsButton.setOnClickListener {
////      val context = holder.itemView.context
////      val intent = Intent(context, PaginaCarteActivity::class.java)
////      intent.putExtra("idCarte", carte.id)
////      context.startActivity(intent)
////    }
////  }
////
////  override fun getItemCount(): Int {
////    return carteList.size
////  }
////
////  class CarteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////    val idCarteTextView: TextView = itemView.findViewById(R.id.text_view_id_carte)
////    val titluTextView: TextView = itemView.findViewById(R.id.text_view_title)
////    val autorTextView: TextView = itemView.findViewById(R.id.text_view_author)
////    val viewDetailsButton: Button = itemView.findViewById(R.id.btn_view_details)
////  }
////}
//
////package com.canhub.cropper.sample.bazadedate
////
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import android.widget.TextView
////import androidx.recyclerview.widget.RecyclerView
////import com.example.croppersample.R
////
////class AdaptorCarte(private val carteList: MutableList<Carte>) : RecyclerView.Adapter<AdaptorCarte.CarteViewHolder>() {
////
////  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarteViewHolder {
////    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carte, parent, false)
////    return CarteViewHolder(view)
////  }
////
////  override fun onBindViewHolder(holder: CarteViewHolder, position: Int) {
////    val carte = carteList[position]
////    holder.titleTextView.text = carte.titlu
////    holder.authorTextView.text = carte.autor
////  }
////
////  override fun getItemCount(): Int {
////    return carteList.size
////  }
////
////  fun addCarte(carte: Carte) {
////    carteList.add(carte)
////    notifyItemInserted(carteList.size - 1)
////  }
////
////  fun updateCarte(position: Int, carte: Carte) {
////    carteList[position] = carte
////    notifyItemChanged(position)
////  }
////
////  fun removeCarte(position: Int) {
////    carteList.removeAt(position)
////    notifyItemRemoved(position)
////  }
////
////  class CarteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////    val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
////    val authorTextView: TextView = itemView.findViewById(R.id.text_view_author)
////  }
////}
