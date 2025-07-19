package com.github.liushuixiaoxia.checksdk

import java.io.File

class CheckSoArgs(
    val so: File,
)

class CheckSoResult(
    val so: File,
    var abi: String,
    val algin16k: Boolean,
)


class CheckApkArgs(
    val apk: File,
)

class CheckApkResult(
    val apk: File,
    val apkAlgin: Boolean,
    val soResult: Map<String, CheckSoResult>,
)

interface ICheck {

    fun checkSo(args: CheckSoArgs): CheckSoResult

    fun checkApk(args: CheckApkArgs): CheckApkResult
}