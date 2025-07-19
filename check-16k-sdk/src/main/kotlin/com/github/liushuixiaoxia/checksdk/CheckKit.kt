package com.github.liushuixiaoxia.checksdk

import com.github.liushuixiaoxia.checksdk.core.CheckImpl
import java.io.File

object CheckKit {

    @JvmStatic
    fun check(file: File): CheckResult {
        if (!file.exists()) {
            throw IllegalArgumentException("File does not exist: ${file.absolutePath}")
        }
        val logback = ILogback.DefaultImpl()
        val results = mutableListOf<CheckSoResult>()
        var result = CheckResult(file, results)
        when {
            file.isDirectory -> {
                val ret = CheckImpl(logback).checkDir(file)
                results.addAll(ret)
            }

            file.name.endsWith(".apk") || file.name.endsWith(".aar") -> {
                val ret = CheckImpl(logback).checkApk(CheckArtefactArgs(file))
                results.addAll(ret.list)
                result = result.copy(apkAlgin = ret.apkAlgin)
            }

            file.name.endsWith(".so") -> {
                val ret = CheckImpl(logback).checkSo(CheckSoArgs(file))
                results.add(ret)
            }

            else -> {
                throw IllegalArgumentException("unsupported file: $file")
            }
        }
        return result
    }
}