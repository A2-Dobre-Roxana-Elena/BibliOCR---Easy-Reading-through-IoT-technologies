package com.canhub.cropper.sample

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.croppersample.R
import com.example.croppersample.databinding.FragmentCameraBinding
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


internal class SampleCropFragment : Fragment() {
  private var _binding: FragmentCameraBinding? = null
  private val binding get() = _binding!!

  private var outputUri: Uri? = null
  private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
    if (it) {
      startCameraWithUri()
    } else {
      showErrorMessage("taking picture failed")
    }
  }

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    when {
      result.isSuccessful -> {
        Timber.tag("AIC-Sample").i("Original bitmap: ${result.originalBitmap}")
        Timber.tag("AIC-Sample").i("Original uri: ${result.originalUri}")
        Timber.tag("AIC-Sample").i("Output bitmap: ${result.bitmap}")
        Timber.tag("AIC-Sample").i("Output uri: ${result.getUriFilePath(requireContext())}")
        handleCropImageResult(result.uriContent.toString())
        processCroppedImage(result.uriContent!!)
      }
      result is CropImage.CancelledResult -> showErrorMessage("cropping image was cancelled by the user")
      else -> showErrorMessage("cropping image failed")
    }
  }

  private val customCropImage = registerForActivityResult(CropImageContract()) {
    if (it !is CropImage.CancelledResult) {
      handleCropImageResult(it.uriContent.toString())
      processCroppedImage(it.uriContent!!)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.takePictureBeforeCallLibraryWithUri.setOnClickListener {
      setupOutputUri()
      takePicture.launch(outputUri)
    }
    //// asta e ce ai tu nevoie
    binding.callLibraryWithoutUri.setOnClickListener {
      startCameraWithoutUri(includeCamera = true, includeGallery = true)
    }
    binding.callLibraryWithoutUriCameraOnly.setOnClickListener {
      startCameraWithoutUri(includeCamera = true, includeGallery = false)
    }
    binding.callLibraryWithoutUriGalleryOnly.setOnClickListener {
      startCameraWithoutUri(includeCamera = false, includeGallery = true)
    }
  }

  private fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
    customCropImage.launch(
      CropImageContractOptions(
        uri = null,
        cropImageOptions = CropImageOptions(
          imageSourceIncludeCamera = includeCamera,
          imageSourceIncludeGallery = includeGallery,
        ),
      ),
    )
  }

  private fun startCameraWithUri() {
    cropImage.launch(
      CropImageContractOptions(
        uri = outputUri,
        cropImageOptions = CropImageOptions(),
      ),
    )
  }

  private fun showErrorMessage(message: String) {
    Timber.tag("AIC-Sample").e("Camera error: $message")
    Toast.makeText(activity, "Crop failed: $message", Toast.LENGTH_SHORT).show()
  }

  private fun handleCropImageResult(uri: String) {
    SampleResultScreen.start(this, null, Uri.parse(uri.replace("file:", "")), null)
  }

  private fun processCroppedImage(uri: Uri) {
    val mPreviewIv: ImageView? = view?.findViewById(R.id.previewImageView)

    if (mPreviewIv != null) {
      mPreviewIv.setImageURI(uri)
    } else {
      Timber.e("ImageView not found")
      return
    }

    val bitmapDrawable = mPreviewIv.drawable as? BitmapDrawable
    val bitmap = bitmapDrawable?.bitmap

    if (bitmap == null) {
      Timber.e("Bitmap is null")
      return
    }

    val recognizer = TextRecognizer.Builder(requireContext()).build()

    if (!recognizer.isOperational) {
      Toast.makeText(requireContext(), "Error: Text recognizer is not operational", Toast.LENGTH_SHORT).show()
      return
    } else {
      val frame = Frame.Builder().setBitmap(bitmap).build()
      val items: SparseArray<TextBlock> = recognizer.detect(frame)
      val sb = StringBuilder()

      for (i in 0 until items.size()) {
        val myItem: TextBlock = items.valueAt(i)
        sb.append(myItem.value)
        sb.append("\n")
      }

      val mResultEt: EditText? = view?.findViewById(R.id.ResultEt)
      mResultEt?.setText(sb.toString())
    }
  }

//
//  private fun processCroppedImage(uri: Uri) {
//    val mPreviewIv: ImageView? = view?.findViewById(R.id.previewImageView)
//
//    if (mPreviewIv != null) {
//      mPreviewIv.setImageURI(uri)
//    } else {
//      Timber.e("ImageView not found")
//      return
//    }
//
//    val bitmapDrawable = mPreviewIv.drawable as? BitmapDrawable
//    val bitmap = bitmapDrawable?.bitmap
//
//    if (bitmap == null) {
//      Timber.e("Bitmap is null")
//      return
//    }
//
//    val recognizer = TextRecognizer.Builder(requireContext()).build()
//
//    if (!recognizer.isOperational) {
//      Toast.makeText(requireContext(), "Error: Text recognizer is not operational", Toast.LENGTH_SHORT).show()
//      return
//    } else {
//      val frame = Frame.Builder().setBitmap(bitmap).build()
//      val items: SparseArray<TextBlock> = recognizer.detect(frame)
//      val sb = StringBuilder()
//
//      for (i in 0 until items.size()) {
//        val myItem: TextBlock = items.valueAt(i)
//        sb.append(myItem.value)
//        sb.append("\n")
//      }
//
//      val mResultEt: EditText? = view?.findViewById(R.id.ResultEt)
//      mResultEt?.setText(sb.toString())
//    }
//  }

  private fun setupOutputUri() {
    if (outputUri == null) {
      context?.let { ctx ->
        val authorities = "${ctx.applicationContext?.packageName}$AUTHORITY_SUFFIX"
        outputUri = FileProvider.getUriForFile(ctx, authorities, createImageFile())
      }
    }
  }

  private fun createImageFile(): File {
    val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
    val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
      "$FILE_NAMING_PREFIX${timeStamp}$FILE_NAMING_SUFFIX",
      FILE_FORMAT,
      storageDir,
    )
  }

  companion object {
    const val DATE_FORMAT = "yyyyMMdd_HHmmss"
    const val FILE_NAMING_PREFIX = "JPEG_"
    const val FILE_NAMING_SUFFIX = "_"
    const val FILE_FORMAT = ".jpg"
    const val AUTHORITY_SUFFIX = ".cropper.fileprovider"
  }
}
