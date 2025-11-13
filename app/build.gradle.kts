plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "cl.duoc.gameverse"
    compileSdk = 36 // Mantener el SDK más reciente es buena práctica

    defaultConfig {
        applicationId = "cl.duoc.gameverse"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Habilitar el vector drawable es importante para Compose
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    // Habilitar Compose
    buildFeatures {
        compose = true
    }
    // Definir la versión del compilador de Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // Asegúrate que esta versión es compatible con tu versión de Kotlin
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependencias base (del catálogo de versiones)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material) // Esta es de Material Components para Vistas, no para Compose
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- DEPENDENCIAS DE JETPACK COMPOSE CORREGIDAS ---

    // Define una versión de Compose para mantener la consistencia
    val composeVersion = "1.6.8"
    val activityComposeVersion = "1.9.0"
    val lifecycleVersion = "2.8.3"
    val navigationVersion = "2.7.7"
    val material3Version = "1.2.1"

    // Activity Compose (Actualizada)
    implementation("androidx.activity:activity-compose:$activityComposeVersion")

    // Compose UI (Versiones alineadas)
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    // Material Design 3 (Actualizada y es la única dependencia de Material para Compose)
    implementation("androidx.compose.material3:material3:$material3Version")

    // ELIMINADA: La librería de íconos de Material 2 ya no es necesaria
    // implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Opcional: Integración con ViewModel (Actualizada)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    // Opcional: Navegación con Compose (Actualizada)
    implementation("androidx.navigation:navigation-compose:$navigationVersion")

    //Login con Google
    implementation("com.google.android.gms:play-services-auth:21.0.0")
}
