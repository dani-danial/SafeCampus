// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    // ✅ This tells Gradle exactly which version of Google Services to use
    id("com.google.gms.google-services") version "4.4.1" apply false
}