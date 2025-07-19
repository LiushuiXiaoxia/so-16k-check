package com.github.liushuixiaoxia.check16k

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.github.liushuixiaoxia.check16k.util.isAndroidApp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging

class Check16kPlugin : Plugin<Project> {

    private val logger = Logging.getLogger(Check16kPlugin::class.java)

    override fun apply(project: Project) {
        if (!project.isAndroidApp()) {
            throw IllegalStateException("${Check16kPlugin::class.java.name} must be applied after 'com.android.application'")
        }

        val extension = project.extensions.create("check16k", Check16KTaskExtension::class.java)
        project.afterEvaluate {
            logger.lifecycle("extension = ${extension.toText()}")
        }

        val android = project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
        android.onVariants { variant ->
            if (!extension.checkEnable()) {
                logger.quiet("check16k is disabled")
                return@onVariants
            }

            val capitalizedVariant = variant.name.replaceFirstChar { it.uppercaseChar() }
            val taskName = "check${capitalizedVariant}So16k"
            val assembleTaskName = "assemble$capitalizedVariant"
            val tp = project.tasks.register(taskName, Check16KTask::class.java) {
                it.getBuildDir().set(project.layout.buildDirectory.get().asFile)
                it.getApkDir().set(variant.artifacts.get(SingleArtifact.APK).get().asFile)
                it.ignoreError = extension.isIgnoreError()
            }
            project.afterEvaluate {
                project.tasks.named(assembleTaskName).configure {
                    it.finalizedBy(tp)
                }
            }
        }
    }
}