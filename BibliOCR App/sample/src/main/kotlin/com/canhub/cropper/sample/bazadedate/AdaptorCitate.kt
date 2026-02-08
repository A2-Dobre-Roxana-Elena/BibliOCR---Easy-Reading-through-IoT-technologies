package com.canhub.cropper.sample.bazadedate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.croppersample.R


class AdaptorCitate(  private val citateList: List<Citate>,
                      private val onItemClick: (Citate) -> Unit
) : RecyclerView.Adapter<AdaptorCitate.CitatViewHolder>() {


//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarteViewHolder {
//    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carte, parent, false)
//    return CarteViewHolder(view)
//  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitatViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_citate, parent, false)
    return CitatViewHolder(view)
  }

//  override fun onBindViewHolder(holder: CarteViewHolder, position: Int) {
//    val carte = carteList[position]
//    holder.titluTextView.text = carte.titlu
//    holder.autorTextView.text = carte.autor
//    holder.idTextView.text = carte.id
//
//    holder.itemView.setOnClickListener {
//      onItemClick(carte)
//    }
//  }

  override fun onBindViewHolder(holder: CitatViewHolder, position: Int) {
    val citat = citateList[position]
    holder.citatTextView.text = citat.quoteText
    holder.idCarteTextView.text = citat.id

    holder.itemView.setOnClickListener {
      onItemClick(citat)
    }
  }

//

//  inner class CarteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val titluTextView: TextView = itemView.findViewById(R.id.text_view_title)
//    val autorTextView: TextView = itemView.findViewById(R.id.text_view_author)
//    val idTextView:TextView = itemView.findViewById(R.id.text_view_id);
//  }



//  override fun getItemCount(): Int {
    //    return carteList.size
//  }
  override fun getItemCount(): Int {
    return citateList.size
  }

//  fun updateData(newCitateList: List<Citate>) {
//    val oldSize = citateList.size
//    citateList.clear()
//    notifyItemRangeRemoved(0, oldSize)
//
//    citateList.addAll(newCitateList)
//    notifyItemRangeInserted(0, newCitateList.size)
//  }
//
//  fun addCitat(citat: Citate) {
//    citateList.add(citat)
//    notifyItemInserted(citateList.size - 1)
//  }
//
//  fun updateCitat(position: Int, citat: Citate) {
//    citateList[position] = citat
//    notifyItemChanged(position)
//  }
//
//  fun removeCitat(position: Int) {
//    citateList.removeAt(position)
//    notifyItemRemoved(position)
//  }

  class CitatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val citatTextView: TextView = itemView.findViewById(R.id.text_view_author)
    val idCarteTextView: TextView = itemView.findViewById(R.id.text_view_title)
  }
}


//package com.canhub.cropper.sample.bazadedate
//
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.croppersample.R
//
//class AdaptorCitate(private val citateList: MutableList<Citate>) : RecyclerView.Adapter<AdaptorCitate.CitatViewHolder>() {
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitatViewHolder {
//    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carte, parent, false)
//    return CitatViewHolder(view)
//  }
//
//  override fun onBindViewHolder(holder: CitatViewHolder, position: Int) {
//    val citat = citateList[position]
//    holder.citatTextView.text = citat.quoteText
//    holder.idCarteTextView.text = citat.idCarte
//  }
//
//  override fun getItemCount(): Int {
//    return citateList.size
//  }
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
//
//  class CitatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val citatTextView: TextView = itemView.findViewById(R.id.text_view_title)
//    val idCarteTextView: TextView = itemView.findViewById(R.id.text_view_author)
//  }
//}
//
