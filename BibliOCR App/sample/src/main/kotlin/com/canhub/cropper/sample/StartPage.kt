
package com.canhub.cropper.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.croppersample.R

class StartPage : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val loginButton: Button = findViewById(R.id.btn_login)
    val registerButton: Button = findViewById(R.id.btn_register)

    loginButton.setOnClickListener {
      startActivity(Intent(this, LoginActivity::class.java))
    }

    registerButton.setOnClickListener {
      startActivity(Intent(this, AddUserActivity::class.java))
    }
  }
}

