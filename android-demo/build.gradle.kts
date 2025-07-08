plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

subprojects {
    plugins.withId("com.android.application") {
        configure<com.android.build.gradle.AppExtension> {
            compileSdkVersion(35)
            defaultConfig {
                minSdk = 24
                targetSdk = 35
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            // ndkVersion = "27.2.12479018"
            ndkVersion = "28.1.13356709"
        }
    }

    plugins.withId("com.android.library") {
        configure<com.android.build.gradle.LibraryExtension> {
            compileSdkVersion(35)
            defaultConfig {
                minSdk = 24
                targetSdk = 35
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            // ndkVersion = "27.2.12479018"
            ndkVersion = "28.1.13356709"
        }
    }
}