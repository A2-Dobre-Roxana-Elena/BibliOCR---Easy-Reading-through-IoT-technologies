package com.canhub.cropper.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.Utilizatori
import com.example.croppersample.R
import org.mindrot.jbcrypt.BCrypt
import timber.log.Timber

class AddUserActivity : AppCompatActivity() {

  private lateinit var editTextUsername: EditText
  private lateinit var editTextEmail: EditText
  private lateinit var editTextPassword: EditText
  private lateinit var editTextConfirmPassword: EditText
  private lateinit var checkBoxAdmin: CheckBox
  private lateinit var btnRegister: Button

  private lateinit var bazaDeDate: BazaDeDate

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_user)

    bazaDeDate = BazaDeDate()

    editTextUsername = findViewById(R.id.edit_text_username)
    editTextEmail = findViewById(R.id.edit_text_email)
    editTextPassword = findViewById(R.id.edit_text_password)
    editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password)
    checkBoxAdmin = findViewById(R.id.checkbox_admin)
    btnRegister = findViewById(R.id.btn_register)

    btnRegister.setOnClickListener {
      val username = editTextUsername.text.toString()
      val email = editTextEmail.text.toString()
      val password = editTextPassword.text.toString()
      val confirmPassword = editTextConfirmPassword.text.toString()
      val isAdmin = checkBoxAdmin.isChecked

      if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        Toast.makeText(this, "Introduceți toate câmpurile!", Toast.LENGTH_SHORT).show()
      } else if (password != confirmPassword) {
        Toast.makeText(this, "Parolele nu se potrivesc!", Toast.LENGTH_SHORT).show()
      } else {
        bazaDeDate.fetchUserName(username, { _ ->
          // Dacă utilizatorul există deja
          Toast.makeText(this, "Numele de utilizator este deja folosit! Alegeți un alt nume de utilizator.", Toast.LENGTH_SHORT).show()
        }, { e ->
          // Dacă utilizatorul nu există, adăugăm noul utilizator
          if (e.message == "Utilizatorul nu există") {
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            val newUser = Utilizatori(
              id = "",
              numeUtilizator = username,
              parola = hashedPassword,
              email = email,
              eadmin = isAdmin
            )

            bazaDeDate.addUserWithAutoIncrement(newUser, {
              saveUserSession(newUser.id, newUser.eadmin)
              if (newUser.eadmin) {
                startActivity(Intent(this, AcasaAdminActivity::class.java))
              } else {
                startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
              }
              finish()
            }, { addUserError ->
              Timber.tag("AddUserActivity").e(addUserError, "Eroare la adăugare utilizator:")
              Toast.makeText(this, "Eroare la adăugare utilizator: ${addUserError.message}", Toast.LENGTH_SHORT).show()
            })
          } else {
            Timber.tag("FetchUser").e(e, "Eroare la verificarea utilizatorului:")
            Toast.makeText(this, "Eroare la verificarea utilizatorului: ${e.message}", Toast.LENGTH_SHORT).show()
          }
        })
      }
    }




//
//    btnRegister.setOnClickListener {
//      val username = editTextUsername.text.toString()
//      val email = editTextEmail.text.toString()
//      val password = editTextPassword.text.toString()
//      val confirmPassword = editTextConfirmPassword.text.toString()
//      val isAdmin = checkBoxAdmin.isChecked
//
//      if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//        Toast.makeText(this, "Introduceți toate câmpurile!", Toast.LENGTH_SHORT).show()
//      } else if (password != confirmPassword) {
//        Toast.makeText(this, "Parolele nu se potrivesc!", Toast.LENGTH_SHORT).show()
//      } else {
//        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
//        val newUser = Utilizatori(
//          id = "",
//          numeUtilizator = username,
//          parola = hashedPassword,
//          email = email,
//          eadmin = isAdmin
//        )
//
//
//
//        bazaDeDate.addUserWithAutoIncrement(newUser, {
//          saveUserSession(newUser.id, newUser.eadmin)
//          if (newUser.eadmin) {
//            startActivity(Intent(this, AcasaAdminActivity::class.java))
//          } else {
//            startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
//          }
//          finish()
//        }, { e ->
//          Timber.tag("AddUserActivity").e(e, "Eroare la adăugare utilizator:")
//          Toast.makeText(this, "Eroare la adăugare utilizator: ${e.message}", Toast.LENGTH_SHORT).show()
//        })
//      }
//    }


  }

  private fun saveUserSession(userId: String, isAdmin: Boolean) {
    val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("user_id", userId)
    editor.putBoolean("is_logged_in", true)
    editor.putBoolean("is_admin", isAdmin)
    editor.apply()
  }
}



//package com.canhub.cropper.sample
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.CheckBox
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
//class AddUserActivity : AppCompatActivity() {
//
//  private lateinit var editTextUsername: EditText
//  private lateinit var editTextEmail: EditText
//  private lateinit var editTextPassword: EditText
//  private lateinit var editTextConfirmPassword: EditText
//  private lateinit var checkBoxAdmin: CheckBox
//  private lateinit var btnRegister: Button
//
//  private lateinit var bazaDeDate: BazaDeDate
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_add_user)
//
//    bazaDeDate = BazaDeDate()
//
//    editTextUsername = findViewById(R.id.edit_text_username)
//    editTextEmail = findViewById(R.id.edit_text_email)
//    editTextPassword = findViewById(R.id.edit_text_password)
//    editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password)
//    checkBoxAdmin = findViewById(R.id.checkbox_admin)
//    btnRegister = findViewById(R.id.btn_register)
//
//    btnRegister.setOnClickListener {
//      val username = editTextUsername.text.toString()
//      val email = editTextEmail.text.toString()
//      val password = editTextPassword.text.toString()
//      val confirmPassword = editTextConfirmPassword.text.toString()
//      val isAdmin = checkBoxAdmin.isChecked
//
//      if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//        Toast.makeText(this, "Introduceti toate campurile!", Toast.LENGTH_SHORT).show()
//      } else if (password != confirmPassword) {
//        Toast.makeText(this, "Parolele nu se potrivesc!", Toast.LENGTH_SHORT).show()
//      } else {
//        checkIfUserExists(username, { userExists ->
//          if (userExists) {
//            Toast.makeText(this, "Numele de utilizator este deja utilizat!", Toast.LENGTH_SHORT).show()
//          } else {
//            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
//            val newUser = Utilizatori(
//              id = "",
//              numeUtilizator = username,
//              parola = hashedPassword,
//              email = email,
//              eadmin = isAdmin
//            )
//
//            bazaDeDate.addUserWithAutoIncrement(newUser, {
//              Toast.makeText(this, "Utilizator adăugat cu succes!", Toast.LENGTH_SHORT).show()
//
//              if(isAdmin)
//                startActivity(Intent(this, AcasaAdminActivity::class.java))
//              else
//                startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
//
//              finish()
//            }, { e ->
//              Timber.tag("RegisterActivity").e(e, "Eroare la adăugare utilizator:")
//              Toast.makeText(this, "Eroare la adăugare utilizator: ${e.message}", Toast.LENGTH_SHORT).show()
//            })
//          }
//        }, { e ->
//          Timber.tag("RegisterActivity").e(e, "Eroare la verificare utilizator:")
//          Toast.makeText(this, "Eroare la verificare utilizator: ${e.message}", Toast.LENGTH_SHORT).show()
//        })
//      }
//    }
//  }
//
//  private fun checkIfUserExists(username: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
//    val db = FirebaseFirestore.getInstance()
//    val usersRef = db.collection("utilizatori")
//    usersRef.whereEqualTo("numeUtilizator", username).get()
//      .addOnSuccessListener { documents ->
//        if (!documents.isEmpty) {
//          onSuccess(true)
//        } else {
//          onSuccess(false)
//        }
//      }
//      .addOnFailureListener { exception ->
//        onFailure(exception)
//      }
//  }
//}
