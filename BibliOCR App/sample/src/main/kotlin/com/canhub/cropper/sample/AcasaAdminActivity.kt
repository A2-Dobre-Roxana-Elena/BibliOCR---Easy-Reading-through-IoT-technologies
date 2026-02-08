//package com.canhub.cropper.sample
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.canhub.cropper.sample.bazadedate.AdaptorCarte
//import com.canhub.cropper.sample.bazadedate.BazaDeDate
//import com.canhub.cropper.sample.bazadedate.Carte
//import com.example.croppersample.R
//import timber.log.Timber
//

package com.canhub.cropper.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.sample.bazadedate.AdaptorCarte
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.Carte
import com.example.croppersample.R
import timber.log.Timber

class AcasaAdminActivity : AppCompatActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var carteAdapter: AdaptorCarte
  private lateinit var carteList: MutableList<Carte>
  private lateinit var bazaDeDate: BazaDeDate

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_acasa_admin)

    recyclerView = findViewById(R.id.recycler_view_books_admin)
    recyclerView.layoutManager = LinearLayoutManager(this)

    carteList = mutableListOf()
    carteAdapter = AdaptorCarte(carteList) { carte ->
      val intent = Intent(this, PaginaCarteActivity::class.java)
      intent.putExtra("idCarte", carte.id)
      intent.putExtra("isAdmin", true)
      startActivity(intent)
    }
    recyclerView.adapter = carteAdapter

    bazaDeDate = BazaDeDate()

    fetchBooksFromDatabase()

    val btnAddBook: Button = findViewById(R.id.btn_add_book)
    btnAddBook.setOnClickListener {
      val intent = Intent(this, AddBookActivity::class.java)
      startActivity(intent)
    }

    val btnLogout: Button = findViewById(R.id.btn_logout)
    btnLogout.setOnClickListener {
      LoginActivity.clearUserSession(this)
      startActivity(Intent(this, StartPage::class.java))
      finish()
    }
  }

  private fun fetchBooksFromDatabase() {
    bazaDeDate.fetchBooks(
      { carti ->
        carteList.clear()
        carteList.addAll(carti)
        carteList.forEachIndexed { index, _ ->
          carteAdapter.notifyItemInserted(index)
        }
      },
      { e ->
        Timber.e("Error fetching books din ACASA ADMIN: $e")
      }
    )
  }
}


//class AcasaAdminActivity : AppCompatActivity() {
//
//  private lateinit var recyclerView: RecyclerView
//  private lateinit var carteAdapter: AdaptorCarte
//  private lateinit var carteList: MutableList<Carte>
//  private lateinit var bazaDeDate: BazaDeDate
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_acasa_admin)
//
//    recyclerView = findViewById(R.id.recycler_view_books_admin)
//    recyclerView.layoutManager = LinearLayoutManager(this)
//
//    carteList = mutableListOf()
//    carteAdapter = AdaptorCarte(carteList)
//    recyclerView.adapter = carteAdapter
//
//    bazaDeDate = BazaDeDate()
//
//    fetchBooksFromDatabase()
//
//    val btnAddBook: Button = findViewById(R.id.btn_add_book)
//    btnAddBook.setOnClickListener {
//      val intent = Intent(this, AddBookActivity::class.java)
//      startActivity(intent)
//    }
//
//    val btnLogout: Button = findViewById(R.id.btn_logout)
//    btnLogout.setOnClickListener {
//      LoginActivity.clearUserSession(this)
//      startActivity(Intent(this, StartPage::class.java))
//      finish()
//    }
//  }
//
//  private fun fetchBooksFromDatabase() {
//    bazaDeDate.fetchBooks(
//      { carti ->
//        carteList.clear()
//        carteList.addAll(carti)
//        carteList.forEachIndexed { index, _ ->
//          carteAdapter.notifyItemInserted(index)
//        }
//      },
//      { e ->
//        Timber.e("Error fetching books din ACASA ADMIN: $e")
//      }
//    )
//  }
//}


//package com.canhub.cropper.sample
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.canhub.cropper.sample.bazadedate.AdaptorCarte
//import com.canhub.cropper.sample.bazadedate.BazaDeDate
//import com.canhub.cropper.sample.bazadedate.Carte
//import com.example.croppersample.R
//import timber.log.Timber
//
//class AcasaAdminActivity : AppCompatActivity() {
//
//  private lateinit var recyclerView: RecyclerView
//  private lateinit var carteAdapter: AdaptorCarte
//  private lateinit var addBookLauncher: ActivityResultLauncher<Intent>
//  private lateinit var carteList: MutableList<Carte>
//  private lateinit var bazaDeDate: BazaDeDate
//
//  companion object {
//    const val ADD_BOOK_REQUEST_CODE = 1
//  }
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_acasa_admin)
//
//    val btnLogout: Button = findViewById(R.id.btn_logout)
//    btnLogout.setOnClickListener {
//      LoginActivity.clearUserSession(this)
//      startActivity(Intent(this, StartPage::class.java))
//      finish()
//    }
//
//    recyclerView = findViewById(R.id.recycler_view_books_admin)
//    recyclerView.layoutManager = LinearLayoutManager(this)
//
//    carteList = mutableListOf()
//    carteAdapter = AdaptorCarte(carteList)
//    recyclerView.adapter = carteAdapter
//
//    bazaDeDate = BazaDeDate()
//    fetchBooksFromDatabase()
//
//    addBookLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//      if (result.resultCode == Activity.RESULT_OK) {
//        fetchBooksFromDatabase()
//      }
//    }
//
//    val addBookButton: Button = findViewById(R.id.btn_add_book)
//    addBookButton.setOnClickListener {
//      val intent = Intent(this, AddBookActivity::class.java)
//      addBookLauncher.launch(intent)
//    }
//
//
////    val addBookButton: Button = findViewById(R.id.btn_add_book)
////    addBookButton.setOnClickListener {
////      startActivity(Intent(this, AddBookActivity::class.java))
////    }
//  }
//
//  @SuppressLint("NotifyDataSetChanged")
//  private fun fetchBooksFromDatabase() {
//    bazaDeDate.fetchBooks({ books ->
//      carteList.clear()
//      carteList.addAll(books)
//      carteAdapter.notifyDataSetChanged()
//    }, { e ->
//      Timber.e("Error fetching books din ACASA ADMIN: $e")
//    })
//  }
//
////  private fun fetchBooksFromDatabase() {
////    bazaDeDate.fetchBooks(
////      { carti ->
////        carteList.clear()
////        carteList.addAll(carti)
////        carteList.forEachIndexed { index, _ ->
////          carteAdapter.notifyItemInserted(index)
////        }
////      },
////      { e ->
////        Timber.e("Error fetching books din ACASA ADMIN: $e")
////      }
////    )
////  }
//
//
//}
