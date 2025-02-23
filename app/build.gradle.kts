import org.jetbrains.kotlin.fir.declarations.builder.buildScript


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"

}

android {
    namespace = "com.example.weatherapplication"
    compileSdk = 35
    viewBinding{
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.weatherapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    implementation (libs.play.services.location)
    implementation (libs.androidx.swiperefreshlayout)
    implementation (libs.gson)
    implementation (libs.gson.v288)
    implementation (libs.play.services.location.v2101)
    implementation (libs.lottie)
    implementation(libs.androidx.navigation.fragment.ktx.v283)
    implementation(libs.androidx.navigation.ui.ktx)
    //RoomDb
    implementation(libs.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)
    implementation (libs.androidx.core.splashscreen)
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)
}

