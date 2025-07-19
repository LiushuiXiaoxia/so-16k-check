package com.github.liushuixiaoxia.checksdk

import com.github.liushuixiaoxia.checksdk.so.SoInfo
import java.io.File

data class CheckResult(
    val file: File,
    val results: MutableList<CheckSoResult>,
    val apkAlgin: Boolean? = null,
)

data class CheckSoArgs(
    val so: File,
)

data class CheckSoResult(
    val file: File,
    var info: SoInfo,
)


data class CheckArtefactArgs(
    val apk: File,
)

data class CheckArtefactResult(
    val file: File,
    val apkAlgin: Boolean? = null,
    val list: List<CheckSoResult> = listOf(),
)

interface ICheck {

    fun checkSo(args: CheckSoArgs): CheckSoResult

    fun checkApk(args: CheckArtefactArgs): CheckArtefactResult

    fun checkDir(file: File): List<CheckSoResult>
}

interface ILogback {
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)

    class DefaultImpl : ILogback {
        override fun info(message: String) {
            println("INFO: $message")
        }

        override fun warn(message: String) {
            println("WARN: $message")
        }

        override fun error(message: String) {
            System.err.println("ERROR: $message")
        }
    }
}