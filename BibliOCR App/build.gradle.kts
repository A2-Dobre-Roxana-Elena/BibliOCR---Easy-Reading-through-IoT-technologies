buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath(libs.plugin.android.cache.fix)
    //classpath(libs.plugin.androidgradleplugin)
    classpath(libs.plugin.dokka)
    classpath(libs.plugin.kotlin)
    classpath(libs.plugin.licensee)
    classpath(libs.plugin.paparazzi)
    classpath(libs.plugin.publish)
    classpath("com.android.tools.build:gradle:8.0.0")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    classpath("com.google.gms:google-services:4.3.10")
  }
}

plugins {
  alias(libs.plugins.codequalitytools)
  id("com.google.gms.google-services") version "4.4.2" apply false
}

codeQualityTools {
  checkstyle {
    enabled = false // Kotlin only.
  }
  pmd {
    enabled = false // Kotlin only.
  }
  ktlint {
    toolVersion = libs.versions.ktlint.get()
  }
  detekt {
    enabled = false // Don"t want.
  }
  cpd {
    enabled = false // Kotlin only.
  }
  lint {
    lintConfig = rootProject.file("lint.xml")
    checkAllWarnings = true
  }
  kotlin {
    allWarningsAsErrors = true
  }
}

allprojects {
  repositories {
    google()
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
    gradlePluginPortal()
  }
}

subprojects {
  plugins.withType<com.android.build.gradle.api.AndroidBasePlugin> {
    apply(plugin = "org.gradle.android.cache-fix")
  }

  tasks.withType(Test::class.java).all {
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
  }
}



