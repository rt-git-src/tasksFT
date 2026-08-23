plugins {
    id("com.android.application")
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
}

ktlint {
    filter {
        exclude { source -> source.file.invariantSeparatorsPath.contains("/generated/") }
    }
}

android {
    compileSdk = 37
    defaultConfig {
        applicationId = "com.rustamft.tasksft"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.2.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets {
        named("debug") {
            kotlin.directories += "build/generated/ksp/debug/kotlin"
        }
        named("release") {
            kotlin.directories += "build/generated/ksp/release/kotlin"
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.rustamft.tasksft"
}

dependencies {

    // Modules
    implementation(project(":domain"))
    implementation(project(":data"))
    // Kotlin extensions for 'core' artifact
    implementation(libs.core.ktx)
    // Lifecycle
    implementation(libs.lifecycle.runtimeKtx)
    implementation(libs.lifecycle.service)
    implementation(libs.lifecycle.viewmodelCompose)
    implementation(libs.lifecycle.runtimeCompose)
    // Activity
    implementation(libs.activity.compose)
    implementation(libs.drawerlayout)
    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.material3)
    implementation(libs.haze)
    androidTestImplementation(libs.compose.uiTestJunit4)
    debugImplementation(libs.compose.debugUiTooling)
    debugImplementation(libs.compose.debugUiTestManifest)
    // Compose destinations
    implementation(libs.composeDestinations.core)
    ksp(libs.composeDestinations.ksp)
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.testJunit4)
    // WorkManager
    implementation(libs.work.runtimeKtx)
    // Coroutines test
    implementation(libs.coroutines.test)
    // LeakCanary
    debugImplementation(libs.leakCanary)
    // JUnit
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit.android)
    // MockK
    testImplementation(libs.mockk)
    // Kaspresso
    androidTestImplementation(libs.kaspresso.core)
    androidTestImplementation(libs.kaspresso.compose)
}
