package com.canhub.cropper.sample.bazadedate


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.croppersample.R
import timber.log.Timber

//import kotlinx.android.synthetic.main.activity_citat_details.*

class PaginaCitatActivity : AppCompatActivity() {

  private lateinit var textView: TextView
  private lateinit var text_view_citat: TextView
  private lateinit var text_book_quote: TextView

  private lateinit var text_view_book_author: TextView
  private lateinit var text_book_book_title: TextView

  private lateinit var text_view_id_user_quote: TextView
  private lateinit var text_titlu: TextView
  private lateinit var text_view_citatul_tau: TextView

  private lateinit var bazaDeDate: BazaDeDate
  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.instanta_citat)

    val citatId = intent.getStringExtra("idCitat")
    val userID = intent.getStringExtra("idUser")
    val ePublic = intent.getBooleanExtra("ePublic", true)
    bazaDeDate = BazaDeDate()

//    val idCarte = intent.getStringExtra("idCarte")
    text_titlu = findViewById(R.id.text_titlu)
    textView = findViewById(R.id.text_view_id_quote)
    text_view_citat = findViewById(R.id.text_view_quote)
    text_book_quote = findViewById(R.id.text_book_quote)


    text_view_book_author = findViewById(R.id.text_book_author_quote)
    text_book_book_title = findViewById(R.id.text_book_title_quote)
    text_view_id_user_quote = findViewById(R.id.text_view_id_user_quote)
    text_view_citatul_tau = findViewById(R.id.text_label_id_user_quote)


    fetchCitatDetailsFromDatabase(citatId!!, { id, quoteText, idCarte, idUser->
      textView.text = id
      text_view_citat.text = quoteText
      text_book_quote.text = idCarte
      text_view_id_user_quote.text = idUser

      if(idUser == userID && !ePublic)
      {
        text_titlu.text = "Citatul tău privat"
        text_view_citatul_tau.text = "Acest citat îți aparține și este privat"
        text_view_id_user_quote.visibility  = View.GONE
      }
      else
      {
        fetchUsersFromDatabase(idUser, {numeUtilizator ->
          //Toast.makeText(this, String.format("3 Numele utilizator e: %s", numeUtilizator), Toast.LENGTH_SHORT).show()

          text_view_id_user_quote.text = numeUtilizator
        }, { e ->
          Timber.e("Error fetching carte details: $e")
        })

      }

      fetchCarteDetailsFromDatabase(idCarte, { titlu, autor, _ ->
        text_book_book_title.text = titlu
        text_view_book_author.text = autor
      }, { e ->
        Timber.e("Error fetching carte details: $e")
      })

    }, { e ->
      Timber.e("Error fetching carte details: $e")
    })
//    val citatId = intent.getStringExtra(citatul.id)
//    val citatText = intent.getStringExtra(citatul.quoteText)


//    textView = findViewById(R.id.text_view_id_quote)
//    text_view_citat = findViewById(R.id.text_view_quote)
//
//    textView.text = citatId.toString()
//    text_view_citat.text =
  }


  private fun fetchUsersFromDatabase(idUser: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
      bazaDeDate.fetchUser(idUser, { user ->
        //Toast.makeText(this, String.format("2 Numele utilizator e: %s", user.numeUtilizator), Toast.LENGTH_SHORT).show()
        onSuccess(user.numeUtilizator)
      }, { e ->
        onFailure(e)
      })
    }

  private fun fetchCitatDetailsFromDatabase(idCitat: String, onSuccess: (String, String, String, String) -> Unit, onFailure: (Exception) -> Unit) {
    bazaDeDate.fetchCitatTitlu(idCitat, { citat ->
      onSuccess(citat.id, citat.quoteText, citat.idCarte, citat.idUser)
    }, { e ->
      onFailure(e)
    })
  }

  private fun fetchCarteDetailsFromDatabase(idCarte: String, onSuccess: (String, String, String) -> Unit, onFailure: (Exception) -> Unit) {
    bazaDeDate.fetchCarteTitlu(idCarte, { carte ->
      onSuccess(carte.titlu, carte.autor, carte.id)
    }, { e ->
      onFailure(e)
    })
  }

}
