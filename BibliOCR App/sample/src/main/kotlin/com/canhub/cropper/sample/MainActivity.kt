package com.canhub.cropper.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.croppersample.R

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val (_, isLoggedIn, isAdmin) = LoginActivity.getUserSession(this)
    if (isLoggedIn) {
      if (isAdmin) {
        startActivity(Intent(this, AcasaAdminActivity::class.java))
      } else {
        startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
      }
    } else {
      startActivity(Intent(this, StartPage::class.java))
    }
    finish()
  }
}


//package com.canhub.cropper.sample
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import com.example.croppersample.R
//
//class MainActivity : AppCompatActivity() {
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_main)
//
//    val loginButton: Button = findViewById(R.id.btn_login)
//    val registerButton: Button = findViewById(R.id.btn_register)
//
//    loginButton.setOnClickListener {
//      startActivity(Intent(this, LoginActivity::class.java))
//    }
//
//    registerButton.setOnClickListener {
//      startActivity(Intent(this, AddUserActivity::class.java))
//    }
//  }
//}
