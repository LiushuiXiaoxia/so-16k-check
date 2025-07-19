package com.github.liushuixiaoxia.checksdk

import java.io.File


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