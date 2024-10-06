buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.0") // Hoặc phiên bản mới hơn nếu cần
        classpath ("com.google.gms:google-services:4.4.2") // Hoặc phiên bản mới hơn nếu cần
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
}