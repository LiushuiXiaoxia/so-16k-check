package com.github.liushuixiaoxia.checksdk.core

import com.github.liushuixiaoxia.checksdk.CheckArtefactArgs
import com.github.liushuixiaoxia.checksdk.ILogback
import org.junit.jupiter.api.Test
import java.io.File

class CheckImplTest {

    @Test
    fun testApk() {
        // val apk = File("z-test/app-debug.apk")
        val apk = File("z-test/app-debug.apk")
        val check = CheckImpl(ILogback.DefaultImpl())
        val ret = check.checkApk(CheckArtefactArgs(apk))
        println("ret = $ret")
    }
}