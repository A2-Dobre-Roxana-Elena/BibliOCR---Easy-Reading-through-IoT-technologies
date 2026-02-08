package com.canhub.cropper.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.croppersample.R

class LoginUserActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login_user)

    val usernameEditText: EditText = findViewById(R.id.edit_text_username)
    val passwordEditText: EditText = findViewById(R.id.edit_text_password)
    val loginButton: Button = findViewById(R.id.btn_login)

    loginButton.setOnClickListener {
      val username = usernameEditText.text.toString()
      val password = passwordEditText.text.toString()

      // Validare și autentificare (momentan trecem peste)
      if (username.isNotEmpty() && password.isNotEmpty()) {
        startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
      }
      startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
    }
  }
}
