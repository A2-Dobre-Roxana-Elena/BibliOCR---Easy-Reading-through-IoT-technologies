plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.parcelize")
  id("com.google.gms.google-services") // Adăugat
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

android {
  namespace = "com.example.croppersample"

  compileSdk = 34

  defaultConfig {
    applicationId = "com.example.croppersample"
    vectorDrawables.useSupportLibrary = true
    minSdk = 21 // Asigură-te că minSdk este 21 sau mai mare
    versionCode = 1
    versionName = "1.0.0"
  }

  buildFeatures {
    viewBinding = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      isShrinkResources = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
    }
  }

  kotlinOptions {
    allWarningsAsErrors = false
  }
}

dependencies {
  implementation(project(":cropper"))
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("androidx.core:core-ktx:1.10.1")
  implementation("com.google.android.material:material:1.9.0")
  implementation("com.jakewharton.timber:timber:5.0.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.activity:activity-ktx:1.6.1")
  implementation("androidx.fragment:fragment-ktx:1.5.7")
  implementation("com.google.android.gms:play-services-vision:20.1.3")

  implementation("com.vanniktech:android-image-cropper:4.5.0")

  implementation("com.google.mlkit:text-recognition:16.0.0")
  implementation("com.google.firebase:firebase-firestore:24.0.0")

  implementation("androidx.recyclerview:recyclerview:1.2.1")
  implementation("org.mindrot:jbcrypt:0.4")

    implementation(libs.androidx.recyclerview)

    // Exclude duplicate classes
  configurations {
    all {
      exclude(group = "com.google.android.gms.internal.vision", module = "play-services-vision-common")
      exclude(group = "com.google.firebase", module = "firebase-ml-common")
    }
  }

  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1") {
    exclude(group = "com.google.android.gms.internal.vision", module = "play-services-vision-common")
    exclude(group = "com.google.android.gms.internal.vision", module = "play-services-vision-image-labeling-internal")
  }
}

// Am eliminat `apply plugin("com.google.gms.google-services")` deoarece este deja specificat în secțiunea `plugins`
