import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.pref)
    alias(libs.plugins.firebase.app.distribution)
    alias(libs.plugins.kotlinx.kover)
}

val signingProperties = loadProperties("$rootDir/signing.properties")
val debug = libs.versions.debug.get()
val release = libs.versions.release.get()
val prod = libs.versions.prod.get()
val staging = libs.versions.staging.get()

android {
    namespace = "com.app.androidcompose"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.app.androidcompose"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create(release) {
            // Remember to edit signing.properties to have the correct info for release build.
            storeFile = file("../config/release.keystore")
            storePassword = signingProperties.getProperty("KEYSTORE_PASSWORD") as String
            keyPassword = signingProperties.getProperty("KEY_PASSWORD") as String
            keyAlias = signingProperties.getProperty("KEY_ALIAS") as String
        }

        getByName(debug) {
            storeFile = file("../config/debug.keystore")
            storePassword = "oQ4mL1jY2uX7wD8q"
            keyAlias = "debug-key-alias"
            keyPassword = "oQ4mL1jY2uX7wD8q"
        }
    }

    buildTypes {
        debug {
            isDefault = true
            isMinifyEnabled = false
            signingConfig = signingConfigs[debug]
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs[release]

            firebaseCrashlytics {
                mappingFileUploadEnabled = true
            }
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create(staging) {
            isDefault = true
            applicationIdSuffix = ".staging"

            firebaseAppDistribution {
                releaseNotes = "Test"
                testers = "full@testers.com"
                groups = "Beta, QA"
            }
        }

        create(prod) {
            applicationIdSuffix = ".prod"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeExtensionVersion.get()
    }

    lint {
        checkDependencies = true
        xmlReport = true
        xmlOutput = file("build/reports/lint/lint-result.xml")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":compose"))
    implementation(project(":note"))

    // Lifecycle
    implementation(libs.bundles.androidx.lifecycle)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.work.runtime.ktx)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Data Store
    implementation(libs.androidx.datastore.preferences)

    // Coroutines
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    // ImmutableList
    implementation(libs.kotlinx.collections.immutable)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.bundles.networking)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Inspect
    debugImplementation(libs.library.chucker)
    releaseImplementation(libs.library.chucker.no.op)

    // Leak Canary
    debugImplementation(libs.squareup.leakcanary)

    // Room
    implementation(libs.bundles.room)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)

    // Coil
    implementation(libs.bundles.coil)

    // Logging
    implementation(libs.timber)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

dependencies {
    kover(project(":compose"))
    kover(project(":note"))
}

kover {
    reports {
        filters {
            includes {
                classes("*ViewModel")
                classes("*UseCase")
                classes("*Mapper")
                classes("*MapperImpl")
                classes("*Repository")
                classes("*RepositoryImpl")
                classes("*Util")
                classes("*Formatter")
                classes("*FormatterImpl")
                classes("*Converter")
                classes("*ConverterImpl")
                classes("*State")
                classes("*Model")
            }
            excludes {
                classes("_")
            }
        }
    }
}
