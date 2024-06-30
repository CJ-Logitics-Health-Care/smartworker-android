buildscript {
    dependencies {
        classpath ("com.google.protobuf:protobuf-gradle-plugin:0.8.18")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.hilt) apply false
}