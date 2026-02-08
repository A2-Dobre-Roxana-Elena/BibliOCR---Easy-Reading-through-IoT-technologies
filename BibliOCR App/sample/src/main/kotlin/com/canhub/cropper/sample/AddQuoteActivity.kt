package com.canhub.cropper.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.sample.bazadedate.BazaDeDate
import com.canhub.cropper.sample.bazadedate.Citate
import com.example.croppersample.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber

class AddQuoteActivity : AppCompatActivity() {

  private lateinit var imageView: ImageView
  private lateinit var editText: EditText
  private lateinit var editTextIdCarte: EditText
  private lateinit var checkboxPersonalQuote: CheckBox

  private val CAMERA_REQUEST_CODE = 100
  private val REQUEST_CODE_PERMISSIONS = 1001
  private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

  private lateinit var bazaDeDate: BazaDeDate

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    if (result.isSuccessful) {
      val croppedUri = result.uriContent
      croppedUri?.let { uri ->
        processCroppedImage(uri, imageView, editText)
      }
    } else {
      Toast.makeText(this, "Cropping failed: ${result.error}", Toast.LENGTH_SHORT).show()
    }
  }

  private lateinit var selectImageFromGalleryLauncher: ActivityResultLauncher<Intent>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_quote)

    bazaDeDate = BazaDeDate()

    imageView = findViewById(R.id.image_view)
    editText = findViewById(R.id.edit_text_citat)
    editTextIdCarte = findViewById(R.id.edit_text_id_carte)
    checkboxPersonalQuote = findViewById(R.id.checkbox_personal_quote)

    val addButton: Button = findViewById(R.id.btn_add)
    addButton.setOnClickListener {

      val idCarte = editTextIdCarte.text.toString()
      val textCitat = editText.text.toString()
      val isPublic = ! checkboxPersonalQuote.isChecked

      //Toast.makeText(this, "0000!", Toast.LENGTH_SHORT).show()
      //Toast.makeText(this, "0!", Toast.LENGTH_SHORT).show()

      if (idCarte.isEmpty() || textCitat.isEmpty()) {
        Toast.makeText(this, "Introduceți toate câmpurile!", Toast.LENGTH_SHORT).show()
      } else {
        //Toast.makeText(this, "1!", Toast.LENGTH_SHORT).show()
        val myCitat = Citate(idCarte = idCarte, quoteText = textCitat, isPublic = isPublic, idUser = getCurrentUserId())
        //Toast.makeText(this, "2!", Toast.LENGTH_SHORT).show()
        bazaDeDate.addQuoteWithAutoIncrement(myCitat, {
          Toast.makeText(this, "Citat adăugat cu succes!", Toast.LENGTH_SHORT).show()
          setResult(Activity.RESULT_OK)
          finish()
          startActivity(Intent(this, AcasaUtilizatorActivity::class.java))
        }, { e ->
          Timber.tag("AddQuoteActivity").e(e, "Eroare la adăugare citat:")
          Toast.makeText(this, "Eroare la adăugare citat: ${e.message}", Toast.LENGTH_SHORT).show()
        })
      }
    }

    selectImageFromGalleryLauncher = registerForActivityResult(
      ActivityResultContracts.StartActivityForResult(),
      ActivityResultCallback { result ->
        if (result.resultCode == Activity.RESULT_OK) {
          val galleryUri = result.data?.data
          galleryUri?.let { uri ->
            launchCropActivity(uri)
          }
        }
      }
    )

    if (!allPermissionsGranted()) {
      ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    if (!isGooglePlayServicesAvailable()) {
      return
    }

    findViewById<Button>(R.id.btn_choose_image).setOnClickListener {
      selectImageFromGallery()
    }

    findViewById<Button>(R.id.btn_take_photo).setOnClickListener {
      checkAndRequestPermissions()
      openCamera()
    }

  }

  private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
  }

  private fun checkAndRequestPermissions() {
    val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val listPermissionsNeeded = mutableListOf<String>()

    if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.CAMERA)
    }
    if (storagePermission != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if (listPermissionsNeeded.isNotEmpty()) {
      ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), CAMERA_REQUEST_CODE)
    }
  }

  private fun openCamera() {
    cropImage.launch(
      CropImageContractOptions(
        null,
        CropImageOptions(
          imageSourceIncludeCamera = true,
          imageSourceIncludeGallery = false,
        ),
      ),
    )
  }

  private fun launchCropActivity(uri: Uri) {
    cropImage.launch(
      CropImageContractOptions(
        uri,
        CropImageOptions(
          imageSourceIncludeGallery = true,
          imageSourceIncludeCamera = false,
        ),
      ),
    )
  }

  private fun processCroppedImage(uri: Uri, imageView: ImageView, editText: EditText) {
    imageView.setImageURI(uri)
    val bitmapDrawable = imageView.drawable as BitmapDrawable
    val bitmap = bitmapDrawable.bitmap

    val image = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    recognizer.process(image)
      .addOnSuccessListener { visionText ->
        val resultText = visionText.text
        editText.setText(resultText)
      }
      .addOnFailureListener { e ->
        Toast.makeText(this, "Text recognition failed: ${e.message}", Toast.LENGTH_SHORT).show()
      }
  }

  fun isGooglePlayServicesAvailable(): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
    if (resultCode != ConnectionResult.SUCCESS) {
      if (googleApiAvailability.isUserResolvableError(resultCode)) {
        googleApiAvailability.getErrorDialog(this, resultCode, 9000)?.show()
      } else {
        Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show()
      }
      return false
    }
    return true
  }

  private fun selectImageFromGallery() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    selectImageFromGalleryLauncher.launch(intent)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_CODE_PERMISSIONS) {
      if (!allPermissionsGranted()) {
        Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
        finish()
      }
    }
  }

  private fun getCurrentUserId(): String {
    val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
    return sharedPreferences.getString("user_id", "") ?: ""
  }
}
