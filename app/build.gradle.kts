plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "mr.demonid.workcalendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "mr.demonid.workcalendar"
        minSdk = 24             // Android 7.0
        targetSdk = 34
        versionCode = 2         // должен увеличиваться при каждом обновлении (для различных xxStore)
        versionName = "1.0.1"   // это версия для показа пользователю

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.flexbox)
    implementation (libs.appcompat.v170)
    implementation (libs.material.v1120)
    implementation(files("libs/ambilwarna-2.0.1.aar"))
}