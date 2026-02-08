package com.canhub.cropper.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.sample.bazadedate.AdaptorCarte
import com.canhub.cropper.sample.bazadedate.AdaptorCitate
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.Carte
import com.canhub.cropper.sample.bazadedate.Citate
import com.canhub.cropper.sample.bazadedate.PaginaCitatActivity
import com.example.croppersample.R
import timber.log.Timber

class AcasaUtilizatorActivity : AppCompatActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var carteAdapter: AdaptorCarte
  private lateinit var carteList: MutableList<Carte>
  private lateinit var bazaDeDate: BazaDeDate

  private lateinit var recyclerViewPrivateQuotes: RecyclerView
  private lateinit var privateQuotesAdapter: AdaptorCitate
  private lateinit var privateQuotesList: MutableList<Citate>

  private lateinit var recyclerViewPublicQuotes: RecyclerView
  private lateinit var publicQuotesAdapter: AdaptorCitate
  private lateinit var publicQuotesList: MutableList<Citate>

  private lateinit var addQuotesLauncher: ActivityResultLauncher<Intent>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_acasa_utilizator)

    val btnLogout: Button = findViewById(R.id.btn_logout)
    btnLogout.setOnClickListener {
      LoginActivity.clearUserSession(this)
      startActivity(Intent(this, StartPage::class.java))
      finish()
    }

    recyclerView = findViewById(R.id.recycler_view_books)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.isNestedScrollingEnabled = false;

    carteList = mutableListOf()
    carteAdapter = AdaptorCarte(carteList) { carte ->
      val intent = Intent(this, PaginaCarteActivity::class.java)
      intent.putExtra("idCarte", carte.id)
      intent.putExtra("isAdmin", false) // Presupunem că utilizatorul nu este admin
      startActivity(intent)
    }
    recyclerView.adapter = carteAdapter

    recyclerViewPrivateQuotes = findViewById(R.id.recycler_view_private_quotes)
    recyclerViewPrivateQuotes.layoutManager = LinearLayoutManager(this)

    privateQuotesList = mutableListOf()
    val currentUserId = getCurrentUserId()
    privateQuotesAdapter = AdaptorCitate(privateQuotesList){ citat ->
      val intent = Intent(this, PaginaCitatActivity::class.java)
      intent.putExtra("idCitat", citat.id)
      intent.putExtra("idUser", currentUserId)
      intent.putExtra("ePublic", false)
      startActivity(intent)
    }
    recyclerViewPrivateQuotes.adapter = privateQuotesAdapter

    recyclerViewPublicQuotes = findViewById(R.id.recycler_view_public_quotes)
    recyclerViewPublicQuotes.layoutManager = LinearLayoutManager(this)

    publicQuotesList = mutableListOf()

    publicQuotesAdapter = AdaptorCitate(publicQuotesList) { citat ->
      val intent = Intent(this, PaginaCitatActivity::class.java)
      intent.putExtra("idCitat",citat.id)
      intent.putExtra("idUser", currentUserId)
      intent.putExtra("ePublic", true)
      startActivity(intent)
    }
    recyclerViewPublicQuotes.adapter = publicQuotesAdapter
    recyclerViewPublicQuotes.isNestedScrollingEnabled = false;

    bazaDeDate = BazaDeDate()

    addQuotesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        fetchQuotesFromDatabase()
      }
    }

    val addBookButton: Button = findViewById(R.id.btn_add_quote)
    addBookButton.setOnClickListener {
      val intent = Intent(this, AddQuoteActivity::class.java)
      addQuotesLauncher.launch(intent)
    }

    fetchQuotesFromDatabase()
    fetchBooksFromDatabase()
  }

  private fun fetchBooksFromDatabase() {
    bazaDeDate.fetchBooks({ carti ->
      val oldSize = carteList.size
      carteList.clear()
      carteAdapter.notifyItemRangeRemoved(0, oldSize)
      carteList.addAll(carti)
      carteAdapter.notifyItemRangeInserted(0, carti.size)
    }, { e ->
      Timber.e("Error fetching books din ACASA UTILIZATOR: $e")
    })
  }

  private fun fetchQuotesFromDatabase() {
    val currentUserId = getCurrentUserId()

    bazaDeDate.fetchQuotes({ publicQuotes ->
      val oldSize = publicQuotesList.size
      publicQuotesList.clear()
      publicQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
      publicQuotesList.addAll(publicQuotes)
      publicQuotesAdapter.notifyItemRangeInserted(0, publicQuotes.size)
    }, { e ->
      Timber.e("Error fetching public quotes: $e")
    })

    bazaDeDate.fetchPrivateQuotes(currentUserId, { privateQuotes ->
      val oldSize = privateQuotesList.size
      privateQuotesList.clear()
      privateQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
      privateQuotesList.addAll(privateQuotes)
      privateQuotesAdapter.notifyItemRangeInserted(0, privateQuotes.size)
    }, { e ->
      Timber.e("Error fetching private quotes: $e")
    })
  }

  private fun getCurrentUserId(): String {
    val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
    return sharedPreferences.getString("user_id", "") ?: ""
  }

  private fun updateBooksList(newBooks: List<Carte>) {
    val oldSize = carteList.size
    carteList.clear()
    carteAdapter.notifyItemRangeRemoved(0, oldSize)
    carteList.addAll(newBooks)
    carteAdapter.notifyItemRangeInserted(0, newBooks.size)
  }

  private fun updatePublicQuotesList(newPublicQuotes: List<Citate>) {
    val oldSize = publicQuotesList.size
    publicQuotesList.clear()
    publicQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
    publicQuotesList.addAll(newPublicQuotes)
    publicQuotesAdapter.notifyItemRangeInserted(0, newPublicQuotes.size)
  }

  private fun updatePrivateQuotesList(newPrivateQuotes: List<Citate>) {
    val oldSize = privateQuotesList.size
    privateQuotesList.clear()
    privateQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
    privateQuotesList.addAll(newPrivateQuotes)
    privateQuotesAdapter.notifyItemRangeInserted(0, newPrivateQuotes.size)
  }
}


//package com.canhub.cropper.sample
//
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
//import com.canhub.cropper.sample.bazadedate.AdaptorCitate
//import com.canhub.cropper.sample.bazadedate.BazaDeDate
//import com.canhub.cropper.sample.bazadedate.Carte
//import com.canhub.cropper.sample.bazadedate.Citate
//import com.example.croppersample.R
//import timber.log.Timber
//
//class AcasaUtilizatorActivity : AppCompatActivity() {
//
//  private lateinit var recyclerView: RecyclerView
//  private lateinit var carteAdapter: AdaptorCarte
//  private lateinit var carteList: MutableList<Carte>
//  private lateinit var bazaDeDate: BazaDeDate
//
//  private lateinit var recyclerViewPrivateQuotes: RecyclerView
//  private lateinit var privateQuotesAdapter: AdaptorCitate
//  private lateinit var privateQuotesList: MutableList<Citate>
//
//  private lateinit var recyclerViewPublicQuotes: RecyclerView
//  private lateinit var publicQuotesAdapter: AdaptorCitate
//  private lateinit var publicQuotesList: MutableList<Citate>
//
//  private lateinit var addQuotesLauncher: ActivityResultLauncher<Intent>
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_acasa_utilizator)
//
//    val btnLogout: Button = findViewById(R.id.btn_logout)
//    btnLogout.setOnClickListener {
//      LoginActivity.clearUserSession(this)
//      startActivity(Intent(this, StartPage::class.java))
//      finish()
//    }
//
//    recyclerView = findViewById(R.id.recycler_view_books)
//    recyclerView.layoutManager = LinearLayoutManager(this)
//
//    carteList = mutableListOf()
//    carteAdapter = AdaptorCarte(carteList)
//    recyclerView.adapter = carteAdapter
//
//    recyclerViewPrivateQuotes = findViewById(R.id.recycler_view_private_quotes)
//    recyclerViewPrivateQuotes.layoutManager = LinearLayoutManager(this)
//
//    privateQuotesList = mutableListOf()
//    privateQuotesAdapter = AdaptorCitate(privateQuotesList)
//    recyclerViewPrivateQuotes.adapter = privateQuotesAdapter
//
//    recyclerViewPublicQuotes = findViewById(R.id.recycler_view_public_quotes)
//    recyclerViewPublicQuotes.layoutManager = LinearLayoutManager(this)
//
//    publicQuotesList = mutableListOf()
//    publicQuotesAdapter = AdaptorCitate(publicQuotesList)
//    recyclerViewPublicQuotes.adapter = publicQuotesAdapter
//
//    bazaDeDate = BazaDeDate()
//
//    addQuotesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//      if (result.resultCode == Activity.RESULT_OK) {
//        fetchQuotesFromDatabase()
//      }
//    }
//
//    val addBookButton: Button = findViewById(R.id.btn_add_quote)
//    addBookButton.setOnClickListener {
//      val intent = Intent(this, AddQuoteActivity::class.java)
//      addQuotesLauncher.launch(intent)
//    }
//
//    fetchQuotesFromDatabase()
//    fetchBooksFromDatabase()
//  }
//
//  private fun fetchBooksFromDatabase() {
//    bazaDeDate.fetchBooks({ carti ->
//      val oldSize = carteList.size
//      carteList.clear()
//      carteAdapter.notifyItemRangeRemoved(0, oldSize)
//      carteList.addAll(carti)
//      carteAdapter.notifyItemRangeInserted(0, carti.size)
//    }, { e ->
//      Timber.e("Error fetching books din ACASA UTILIZATOR: $e")
//    })
//  }
//
//  private fun fetchQuotesFromDatabase() {
//    val currentUserId = getCurrentUserId()
//
//    bazaDeDate.fetchQuotes({ publicQuotes ->
//      val oldSize = publicQuotesList.size
//      publicQuotesList.clear()
//      publicQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
//      publicQuotesList.addAll(publicQuotes)
//      publicQuotesAdapter.notifyItemRangeInserted(0, publicQuotes.size)
//    }, { e ->
//      Timber.e("Error fetching public quotes: $e")
//    })
//
//    bazaDeDate.fetchPrivateQuotes(currentUserId, { privateQuotes ->
//      val oldSize = privateQuotesList.size
//      privateQuotesList.clear()
//      privateQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
//      privateQuotesList.addAll(privateQuotes)
//      privateQuotesAdapter.notifyItemRangeInserted(0, privateQuotes.size)
//    }, { e ->
//      Timber.e("Error fetching private quotes: $e")
//    })
//  }
//
////  private fun fetchBooksFromDatabase() {
////    bazaDeDate.fetchBooks({ carti ->
////      updateBooksList(carti)
////    }, { e ->
////      Timber.e("Error fetching books din ACASA UTILIZATOR: $e")
////    })
////  }
////
////  private fun fetchQuotesFromDatabase() {
////    val currentUserId = getCurrentUserId()
////
////    bazaDeDate.fetchQuotes({ publicQuotes ->
////      updatePublicQuotesList(publicQuotes)
////    }, { e ->
////      Timber.e("Error fetching public quotes: $e")
////    })
////
////    bazaDeDate.fetchPrivateQuotes(currentUserId, { privateQuotes ->
////      updatePrivateQuotesList(privateQuotes)
////    }, { e ->
////      Timber.e("Error fetching private quotes: $e")
////    })
////  }
//
//  private fun getCurrentUserId(): String {
//    val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
//    return sharedPreferences.getString("user_id", "") ?: ""
//  }
//
//  private fun updateBooksList(newBooks: List<Carte>) {
//    val oldSize = carteList.size
//    carteList.clear()
//    carteAdapter.notifyItemRangeRemoved(0, oldSize)
//    carteList.addAll(newBooks)
//    carteAdapter.notifyItemRangeInserted(0, newBooks.size)
//  }
//
//  private fun updatePublicQuotesList(newPublicQuotes: List<Citate>) {
//    val oldSize = publicQuotesList.size
//    publicQuotesList.clear()
//    publicQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
//    publicQuotesList.addAll(newPublicQuotes)
//    publicQuotesAdapter.notifyItemRangeInserted(0, newPublicQuotes.size)
//  }
//
//  private fun updatePrivateQuotesList(newPrivateQuotes: List<Citate>) {
//    val oldSize = privateQuotesList.size
//    privateQuotesList.clear()
//    privateQuotesAdapter.notifyItemRangeRemoved(0, oldSize)
//    privateQuotesList.addAll(newPrivateQuotes)
//    privateQuotesAdapter.notifyItemRangeInserted(0, newPrivateQuotes.size)
//  }
//}
//
////package com.canhub.cropper.sample
////
////import android.app.Activity
////import android.content.Intent
////import android.os.Bundle
////import android.widget.Button
////import androidx.activity.result.ActivityResultLauncher
////import androidx.activity.result.contract.ActivityResultContracts
////import androidx.appcompat.app.AppCompatActivity
////import androidx.recyclerview.widget.LinearLayoutManager
////import androidx.recyclerview.widget.RecyclerView
////import com.canhub.cropper.sample.bazadedate.AdaptorCarte
////import com.canhub.cropper.sample.bazadedate.AdaptorCitate
////import com.canhub.cropper.sample.bazadedate.BazaDeDate
////import com.canhub.cropper.sample.bazadedate.Carte
////import com.canhub.cropper.sample.bazadedate.Citate
////import com.example.croppersample.R
////import timber.log.Timber
////
////class AcasaUtilizatorActivity : AppCompatActivity() {
////
////  private lateinit var bazaDeDate: BazaDeDate
////
////  private lateinit var recyclerView: RecyclerView
////  private lateinit var carteAdapter: AdaptorCarte
////  private lateinit var carteList: MutableList<Carte>
////
////  private lateinit var recyclerViewCitate: RecyclerView
////  private lateinit var citateAdapter: AdaptorCitate
////  private lateinit var citateList: MutableList<Citate>
////
////  private lateinit var recyclerViewCitatePrivate: RecyclerView
////  private lateinit var citatePrivateAdapter: AdaptorCitate
////  private lateinit var citatePrivateList: MutableList<Citate>
////
////
////  private lateinit var addQuotesLauncher: ActivityResultLauncher<Intent>
////
////
////  override fun onCreate(savedInstanceState: Bundle?) {
////    super.onCreate(savedInstanceState)
////    setContentView(R.layout.activity_acasa_utilizator)
////
////    val btnLogout: Button = findViewById(R.id.btn_logout)
////    btnLogout.setOnClickListener {
////      LoginActivity.clearUserSession(this)
////      startActivity(Intent(this, StartPage::class.java))
////      finish()
////    }
////
////    recyclerView = findViewById(R.id.recycler_view_books)
////    recyclerView.layoutManager = LinearLayoutManager(this)
////
////    recyclerViewCitate = findViewById(R.id.recycler_view_public_quotes)
////    recyclerViewCitate.layoutManager = LinearLayoutManager(this)
////
////    recyclerViewCitatePrivate = findViewById(R.id.recycler_view_private_quotes)
////    recyclerViewCitatePrivate.layoutManager = LinearLayoutManager(this)
////
////    carteAdapter = AdaptorCarte(carteList)
////    citateAdapter = AdaptorCitate(citateList)
////    citatePrivateAdapter = AdaptorCitate(citatePrivateList)
////
////    recyclerView.adapter = carteAdapter
////    recyclerViewCitate.adapter = citateAdapter
////    recyclerViewCitate.adapter = citatePrivateAdapter
////
////    carteList = mutableListOf()
////    citateList = mutableListOf()
////    citatePrivateList = mutableListOf()
////
////    bazaDeDate = BazaDeDate()
////
////    addQuotesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
////      if (result.resultCode == Activity.RESULT_OK) {
////        fetchQuotesFromDatabase()
//////        fetchPrivateQuotesFromDatabase()
////      }
////    }
////
////    val addBookButton: Button = findViewById(R.id.btn_add_quote)
////    addBookButton.setOnClickListener {
////      val intent = Intent(this, AddQuoteActivity::class.java)
////      addQuotesLauncher.launch(intent)
////    }
////
////
////    fetchQuotesFromDatabase()
////    fetchBooksFromDatabase()
//////    fetchPrivateQuotesFromDatabase()
////  }
////
////  private fun fetchBooksFromDatabase() {
////    bazaDeDate.fetchBooks({ carti ->
////      carteList.clear()
////      carteList.addAll(carti)
////      carteList.forEachIndexed { index, _ ->
////        carteAdapter.notifyItemInserted(index)
////      }
////    }, { e ->
////      Timber.e("Error fetching books din ACASA UTILIZATOR: $e")
////    })
////  }
////
////  private fun fetchQuotesFromDatabase() {
////    bazaDeDate.fetchQuotes({ citate ->
////      citateList.clear()
////      citateList.addAll(citate)
////      citateList.forEachIndexed { index, _ ->
////        citateAdapter.notifyItemInserted(index)
////      }
////    }, { e ->
////      Timber.e("Error fetching books din ACASA LA CITATE: $e")
////    })
////  }
////
////  private fun fetchPrivateQuotesFromDatabase() {
////    val currentUserId = getUserId()
////
////    bazaDeDate.fetchPrivateQuotes(currentUserId, { citate ->
////      citatePrivateList.clear()
////      citatePrivateList.addAll(citate)
////      citatePrivateList.forEachIndexed { index, _ ->
////        citatePrivateAdapter.notifyItemInserted(index)
////      }
////    }, { e ->
////      Timber.e("Error fetching books din ACASA LA CITATE PRIVATE!!!: $e")
////    })
////  }
////
////  private fun getUserId(): String {
////    val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
////    return sharedPreferences.getString("user_id", "") ?: ""
////  }
////
////}
