package com.canhub.cropper.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.Utilizatori
import com.example.croppersample.R
import com.google.firebase.firestore.FirebaseFirestore
import org.mindrot.jbcrypt.BCrypt
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

  private lateinit var editTextUsername: EditText
  private lateinit var editTextPassword: EditText
  private lateinit var btnLogin: Button

  private lateinit var bazaDeDate: BazaDeDate

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    bazaDeDate = BazaDeDate()

    editTextUsername = findViewById(R.id.edit_text_username)
    editTextPassword = findViewById(R.id.edit_text_password)
    btnLogin = findViewById(R.id.btn_login)

    btnLogin.setOnClickListener {
      val username = editTextUsername.text.toString()
      val password = editTextPassword.text.toString()

      if (username.isEmpty() || password.isEmpty()) {
        Toast.makeText(this, "Introduceți toate câmpurile!", Toast.LENGTH_SHORT).show()
      } else {
        loginUser(username, password)
      }
    }
  }

  private fun loginUser(username: String, password: String) {
    val db = FirebaseFirestore.getInstance()
    val usersRef = db.collection("utilizatori")
    usersRef.whereEqualTo("numeUtilizator", username).get()
      .addOnSuccessListener { documents ->
        if (documents.isEmpty) {
          Toast.makeText(this, "Nume utilizator sau parolă incorecte!", Toast.LENGTH_SHORT).show()
        } else {
          for (document in documents) {
            val user = document.toObject(Utilizatori::class.java)
            if (BCrypt.checkpw(password, user.parola)) {
              saveUserSession(user.id, user.eadmin)
              if (user.eadmin) {
                startActivity(Intent(this, AcasaAdminActivity::class.java))
              } else {
                startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
              }
              finish()
            } else {
              Toast.makeText(this, "Nume utilizator sau parolă incorecte!", Toast.LENGTH_SHORT).show()
            }
          }
        }
      }
      .addOnFailureListener { e ->
        Timber.tag("LoginActivity").e(e, "Eroare la autentificare:")
        Toast.makeText(this, "Eroare la autentificare: ${e.message}", Toast.LENGTH_SHORT).show()
      }
  }

  private fun saveUserSession(userId: String, isAdmin: Boolean) {
    val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("user_id", userId)
    editor.putBoolean("is_logged_in", true)
    editor.putBoolean("is_admin", isAdmin)
    editor.apply()
  }

  companion object {
    fun getUserSession(context: Context): Triple<String?, Boolean, Boolean> {
      val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
      val userId = sharedPreferences.getString("user_id", null)
      val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
      val isAdmin = sharedPreferences.getBoolean("is_admin", false)
      return Triple(userId, isLoggedIn, isAdmin)
    }

    fun clearUserSession(context: Context) {
      val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
      val editor = sharedPreferences.edit()
      editor.clear()
      editor.apply()
    }
  }
}



//package com.canhub.cropper.sample
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.canhub.cropper.sample.bazadedate.BazaDeDate
//import com.canhub.cropper.sample.bazadedate.Utilizatori
//import com.example.croppersample.R
//import com.google.firebase.firestore.FirebaseFirestore
//import org.mindrot.jbcrypt.BCrypt
//import timber.log.Timber
//
//class LoginActivity : AppCompatActivity() {
//
//  private lateinit var editTextUsername: EditText
//  private lateinit var editTextPassword: EditText
//  private lateinit var btnLogin: Button
//
//  private lateinit var bazaDeDate: BazaDeDate
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_login)
//
//    bazaDeDate = BazaDeDate()
//
//    editTextUsername = findViewById(R.id.edit_text_username)
//    editTextPassword = findViewById(R.id.edit_text_password)
//    btnLogin = findViewById(R.id.btn_login)
//
//    btnLogin.setOnClickListener {
//      val username = editTextUsername.text.toString()
//      val password = editTextPassword.text.toString()
//
//      if (username.isEmpty() || password.isEmpty()) {
//        Toast.makeText(this, "Introduceti toate campurile!", Toast.LENGTH_SHORT).show()
//      } else {
//        Toast.makeText(this, "A preluat datele!", Toast.LENGTH_SHORT).show()
//        loginUser(username, password)
//      }
//    }
//  }
//
//  private fun loginUser(username: String, password: String) {
//    val db = FirebaseFirestore.getInstance()
//    val usersRef = db.collection("utilizatori")
//    usersRef.whereEqualTo("numeUtilizator", username).get()
//      .addOnSuccessListener { documents ->
//        if (documents.isEmpty) {
//          Toast.makeText(this, "0!", Toast.LENGTH_SHORT).show()
//        } else {
//          Toast.makeText(this, "1!", Toast.LENGTH_SHORT).show()
//          for (document in documents) {
//            Toast.makeText(this, "2!", Toast.LENGTH_SHORT).show()
//            val user = document.toObject(Utilizatori::class.java)
//
//            if (BCrypt.checkpw(password, user.parola)) {
//              Toast.makeText(this, "A intrat aici!", Toast.LENGTH_SHORT).show()
//              if (user.eadmin) {
//                startActivity(Intent(this, AcasaAdminActivity::class.java))
//              } else {
//                startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
//              }
//              finish()
//            } else {
//              Toast.makeText(this, "Nume utilizator sau parola incorecta -> 3!", Toast.LENGTH_SHORT).show()
//            }
//          }
//        }
//      }
//      .addOnFailureListener { e ->
//        Timber.tag("LoginActivity").e(e, "Eroare la autentificare:")
//        Toast.makeText(this, "Eroare la autentificare: ${e.message}", Toast.LENGTH_SHORT).show()
//      }
//  }
//}
