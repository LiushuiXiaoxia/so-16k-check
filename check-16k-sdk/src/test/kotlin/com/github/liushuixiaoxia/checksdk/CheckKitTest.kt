package com.github.liushuixiaoxia.checksdk

import java.io.File
import kotlin.test.Test

class CheckKitTest {

    @Test
    fun testArtefact() {
        val apk = File("../z-res/app-debug.apk")
        val ret = CheckKit.check(apk)
        println("ret.file = ${ret.file}")
        println("ret.apkAlgin = ${ret.apkAlgin}")
        ret.results.forEach {
            println("ret.result = $it")
        }
    }

    @Test
    fun testFile() {
        val f = File("../z-res/app")
        val ret = CheckKit.check(f)
        println("ret.file = ${ret.file}")
        println("ret.apkAlgin = ${ret.apkAlgin}")

        ret.results.forEach {
            println("ret.result = $it")
        }
    }

    @Test
    fun testSo() {
        val f = File("../z-res/app")

        listOf(
            "lib/arm64-v8a/libc++_shared.so",
            "lib/arm64-v8a/libnlog.so",
            "lib/x86/libc++_shared.so"
        ).forEach { so ->
            val ret = CheckKit.check(File(f, so))
            println("ret.file = ${ret.file}")
            ret.results.forEach {
                println("ret.result = $it")
            }
        }
    }
}