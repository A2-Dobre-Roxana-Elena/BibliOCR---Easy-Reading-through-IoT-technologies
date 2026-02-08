package com.canhub.cropper.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
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
import com.canhub.cropper.sample.bazadedate.Carte
import com.canhub.cropper.sample.bazadedate.InstantaCarte
import com.example.croppersample.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber


class AddBookActivity : AppCompatActivity() {

  private lateinit var imageViewAuthor: ImageView
  private lateinit var editTextAuthor: EditText
  private lateinit var imageViewTitle: ImageView
  private lateinit var editTextTitle: EditText
  private lateinit var imageViewPublisher: ImageView
  private lateinit var editTextPublisher: EditText

  private lateinit var imageViewIsbn: ImageView
  private lateinit var editTextIsbn: EditText

  private lateinit var imageViewYear: ImageView
  private lateinit var editTextYear: EditText

  private val CAMERA_REQUEST_CODE = 100
  private val REQUEST_CODE_PERMISSIONS = 1001
  private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

  private lateinit var bazaDeDate: BazaDeDate

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    if (result.isSuccessful) {
      val croppedUri = result.uriContent
      croppedUri?.let { uri ->
        when (currentSection) {
          Section.AUTHOR -> processCroppedImage(uri, imageViewAuthor, editTextAuthor)
          Section.TITLE -> processCroppedImage(uri, imageViewTitle, editTextTitle)
          Section.PUBLISHER -> processCroppedImage(uri, imageViewPublisher, editTextPublisher)
          Section.ISBN -> processCroppedImage(uri, imageViewIsbn, editTextIsbn)
          Section.YEAR -> processCroppedImage(uri, imageViewYear, editTextYear)
        }
      }
    } else {
      Toast.makeText(this, "Cropping failed: ${result.error}", Toast.LENGTH_SHORT).show()
    }
  }

  private lateinit var selectImageFromGalleryLauncher: ActivityResultLauncher<Intent>
  private var currentSection: Section = Section.AUTHOR

  enum class Section {
    AUTHOR, TITLE, PUBLISHER, ISBN, YEAR
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_book)


    ////
    bazaDeDate = BazaDeDate()

    val addButton: Button = findViewById(R.id.btn_add)
    addButton.setOnClickListener {

      val autor = editTextAuthor.getText().toString()
      val titlu = editTextTitle.getText().toString()
      val editura = editTextPublisher.getText().toString()
      val isbn = editTextIsbn.getText().toString()
      val year = editTextYear.getText().toString()

      if(autor=="" || editura=="" || titlu=="" || year=="" || isbn=="")
      {
        Toast.makeText(this, "Introduceți toate campurile!", Toast.LENGTH_SHORT).show()
      }
      else
      {
        val myBook = Carte(autor = editTextAuthor.getText().toString(), titlu = editTextTitle.getText().toString())

        bazaDeDate.checkIfBookExists(myBook, { bookId ->
          if (bookId == null) {
            // Cartea nu există, o adăugăm și adăugăm instanța
            bazaDeDate.addBookWithAutoIncrement(myBook, { newBookId ->

              val instantaNoua = InstantaCarte(
                id = "",
                idCarte= newBookId,
                isbn = isbn,
                eimprumatata = false,
                codQR = "",
                editura = editura,
                anCarte = year
              )
              bazaDeDate.addBookInstanceWithAutoIncrement(instantaNoua, {
                Toast.makeText(this, "Instanța de carte a fost adăugată cu succes!", Toast.LENGTH_SHORT).show()
              }, { e ->
                Timber.tag("MainActivity").e(e, "%s", "Eroare la adăugare instanță carte :")
              })
            }, { e ->
              Timber.tag("MainActivity").e(e, "%s", "Eroare la adăugare carte :")
            })

            setResult(Activity.RESULT_OK)
            finish()

          } else {
            // Cartea există, adăugăm doar instanța
            val instantaNoua = InstantaCarte(
              id = "",
              idCarte= bookId,
              isbn = isbn,
              eimprumatata = false,
              codQR = "",
              editura = editura,
              anCarte = year
            )
            bazaDeDate.addBookInstanceWithAutoIncrement(instantaNoua, {
              Toast.makeText(this, "Instanța de carte a fost adăugată cu succes!", Toast.LENGTH_SHORT).show()
            }, { e ->
              Timber.tag("MainActivity").e(e, "%s", "Eroare la adăugare instanță carte:")
            })
          }
        }, { e ->
          Timber.tag("MainActivity").e(e, "%s", "Eroare la căutarea cărții")
        })

        startActivity(Intent(this, AcasaAdminActivity::class.java))

      }
    }


    imageViewAuthor = findViewById(R.id.image_view_author)
    editTextAuthor = findViewById(R.id.edit_text_author)

    imageViewTitle = findViewById(R.id.image_view_title)
    editTextTitle = findViewById(R.id.edit_text_title)

    imageViewPublisher = findViewById(R.id.image_view_publisher)
    editTextPublisher = findViewById(R.id.edit_text_publisher)

    imageViewIsbn = findViewById(R.id.image_view_isbn)
    editTextIsbn = findViewById(R.id.edit_text_isbn)

    imageViewYear = findViewById(R.id.image_view_year)
    editTextYear = findViewById(R.id.edit_an_publicare)

    selectImageFromGalleryLauncher = registerForActivityResult(
      ActivityResultContracts.StartActivityForResult(),
      ActivityResultCallback { result ->
        if (result.resultCode == Activity.RESULT_OK) {
          val galleryUri = result.data?.data
          galleryUri?.let { uri ->
            launchCropActivity(uri)
          }
        }
      },
    )

    if (!allPermissionsGranted()) {
      ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    if (!isGooglePlayServicesAvailable()) {
      return
    }

    findViewById<Button>(R.id.btn_choose_image_author).setOnClickListener {
      currentSection = Section.AUTHOR
      selectImageFromGallery()
    }

    findViewById<Button>(R.id.btn_take_photo_author).setOnClickListener {
      currentSection = Section.AUTHOR
      checkAndRequestPermissions()
      openCamera()
    }

    findViewById<Button>(R.id.btn_choose_image_title).setOnClickListener {
      currentSection = Section.TITLE
      selectImageFromGallery()
    }

    findViewById<Button>(R.id.btn_take_photo_title).setOnClickListener {
      currentSection = Section.TITLE
      checkAndRequestPermissions()
      openCamera()
    }

    findViewById<Button>(R.id.btn_choose_image_publisher).setOnClickListener {
      currentSection = Section.PUBLISHER
      selectImageFromGallery()
    }

    findViewById<Button>(R.id.btn_take_photo_publisher).setOnClickListener {
      currentSection = Section.PUBLISHER
      checkAndRequestPermissions()
      openCamera()
    }

    findViewById<Button>(R.id.btn_choose_image_isbn).setOnClickListener {
      currentSection = Section.ISBN
      selectImageFromGallery()
    }

    findViewById<Button>(R.id.btn_take_photo_isbn).setOnClickListener {
      currentSection = Section.ISBN
      checkAndRequestPermissions()
      openCamera()
    }

    findViewById<Button>(R.id.btn_choose_image_year).setOnClickListener {
      currentSection = Section.YEAR
      selectImageFromGallery()
    }

    findViewById<Button>(R.id.btn_take_photo_year).setOnClickListener {
      currentSection = Section.YEAR
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
        // Handle the error here
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

}

