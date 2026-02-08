package com.canhub.cropper.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.croppersample.R

class LoginAdminActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login_admin)

    val adminNameEditText: EditText = findViewById(R.id.edit_text_adminname)
    val adminPasswordEditText: EditText = findViewById(R.id.edit_text_adminpassword)
    val loginButton: Button = findViewById(R.id.btn_login_admin)

    loginButton.setOnClickListener {
      val adminName = adminNameEditText.text.toString()
      val adminPassword = adminPasswordEditText.text.toString()

      // Validare și autentificare (momentan trecem peste)
      if (adminName.isNotEmpty() && adminPassword.isNotEmpty()) {
        startActivity(Intent(this, AcasaAdminActivity::class.java))
      }
      startActivity(Intent(this, AcasaAdminActivity::class.java))
    }
  }
}
