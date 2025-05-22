package com.github.liushuixiaoxia.check16k

import com.github.liushuixiaoxia.processkit.ProcessCallback
import com.github.liushuixiaoxia.processkit.ProcessKit
import com.github.liushuixiaoxia.processkit.ProcessResult
import com.github.liushuixiaoxia.processkit.ResultLine
import com.github.liushuixiaoxia.processkit.runCmd
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject


abstract class Check16KTask : DefaultTask() {

    private val logger = Logging.getLogger(Check16KTask::class.java)

    @Internal
    lateinit var buildDir: File

    @Internal
    lateinit var apkDir: File

    @Internal
    var ignoreError: Boolean = true

    @TaskAction
    fun action() {
        val f = apkDir
        logger.lifecycle("apkDir = $f")
        val apk = f.walkTopDown().firstOrNull { it.name.endsWith(".apk") }
        if (apk == null || !apk.exists()) {
            throw RuntimeException("apk is not found, apk = $apk")
        }
        logger.lifecycle("apk = $apk")

        val shell = File(buildDir, "intermediates/check-so/check-so.sh").apply {
            parentFile.mkdirs()
        }
        val s = this.javaClass.classLoader.getResourceAsStream("check-so.sh")?.bufferedReader()?.readText()
            ?: throw RuntimeException("check-so.sh is not found")
        shell.writeText(s)

        val ret = ProcessKit.run("bash ${shell.absolutePath} ${apk.absolutePath}")
        if (ret == 0) {
            logger.quiet("$name success")
        } else {
            if (ignoreError) {
                logger.quiet("$name fail, but ignore")
            } else {
                logger.quiet("$name fail")
                throw RuntimeException("$name fail")
            }
        }
    }
}