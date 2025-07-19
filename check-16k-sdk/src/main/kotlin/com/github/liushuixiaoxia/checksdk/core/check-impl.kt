package com.github.liushuixiaoxia.checksdk.core

import com.github.liushuixiaoxia.checksdk.CheckArtefactArgs
import com.github.liushuixiaoxia.checksdk.CheckArtefactResult
import com.github.liushuixiaoxia.checksdk.CheckSoArgs
import com.github.liushuixiaoxia.checksdk.CheckSoResult
import com.github.liushuixiaoxia.checksdk.ICheck
import com.github.liushuixiaoxia.checksdk.ILogback
import com.github.liushuixiaoxia.checksdk.SoInfo
import com.github.liushuixiaoxia.checksdk.util.unzip
import java.io.File
import java.nio.file.Files

class CheckImpl(val logback: ILogback) : ICheck {

    override fun checkSo(args: CheckSoArgs): CheckSoResult {
        val f = args.so
        val info = SoInfo.parse(args.so)
        return CheckSoResult(f.canonicalFile, info)
    }

    override fun checkApk(args: CheckArtefactArgs): CheckArtefactResult {
        val temp = Files.createTempDirectory("check-so-").toFile()
        args.apk.unzip(temp)
        val list = checkDir(temp)
        val apkAlgin = checkSoAlignment(args.apk).all { it.value.second }
        return CheckArtefactResult(args.apk, apkAlgin, list).apply {
            temp.deleteOnExit()
        }
    }

    override fun checkDir(file: File): List<CheckSoResult> {
        val soList = file.walkBottomUp().filter { it.isFile }.filter { it.extension == "so" }.sorted()
        return soList.map {
            val info = SoInfo.parse(it)
            CheckSoResult(it.relativeTo(file), info)
        }.toList()
    }
}