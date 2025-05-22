package com.github.liushuixiaoxia.check16k.util

import org.gradle.api.Project

fun Project.isAndroidApp(): Boolean {
    return this.plugins.hasPlugin("com.android.application")
}