# so-16k-check

---

Android apk so 16k check gradle plugin.


```kotlin
// build.gradle.kts
// android application project
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.github.liushuixiaoxia.check16k")
}

check16k {
    enable.set(true) // default is true
    // ignoreError.set(false) // default is false
}
```