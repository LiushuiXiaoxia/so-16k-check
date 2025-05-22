plugins {
    `java-gradle-plugin`
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    compileOnly(libs.agpDev)
}

gradlePlugin {
    plugins {
        create("check16k") {
            id = "com.github.liushuixiaoxia.check16k"
            implementationClass = "com.github.liushuixiaoxia.check16k.Check16kPlugin"
        }
    }
}