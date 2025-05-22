package com.github.liushuixiaoxia.check16k

import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
//import com.android.build.api.artifact.ArtifactType
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class Check16kPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 确保应用了 com.android.application 插件
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw IllegalStateException("MyApkProcessorPlugin must be applied after 'com.android.application'")
        }

//        // 获取 Android 扩展
//        val android = project.extensions.getByType(AndroidComponentsExtension::class.java)
//
//        android.onVariants { variant ->
//            // 注册在 APK 打包完成后执行的任务
//            variant.artifacts.use(
//                action = object : com.android.build.api.artifact.SingleArtifactProcessor<File> {
//                    override fun process(input: File) {
//                        println("✅ Built APK for variant [${variant.name}]: ${input.absolutePath}")
//                        // TODO: 你可以在这里复制、上传、记录等
//                    }
//                }
//            ).wiredWithSingleOutput().toTransform(ArtifactType.APK)
//        }
    }
}