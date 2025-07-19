package com.github.liushuixiaoxia.checksdk.core

import com.github.liushuixiaoxia.cmdkit.CmdKit
import java.io.File

fun isElfPageAlignedV2(file: File): Boolean {
    val cmd = "objdump -p ${file.absolutePath}"
    val ret = CmdKit.call(cmd)
    if (!ret.isSuccess()) {
        return false
    }
    Thread.sleep(10)
    val s = ret.all.firstOrNull { it.contains("LOAD") } ?: return false
    // println(s)
    // LOAD off    0x0000000000000000 vaddr 0x0000000000000000 paddr 0x0000000000000000 align 2**14
    val regex = Regex("""align\s+\d\*\*\d+""")
    val match = regex.find(s)
    val result = match?.value ?: return false // 提取 "2**14"
    val s2 = result.replace("align", "").trim()
    val n = s2.split("*").last().toIntOrNull() ?: return false
    return n >= 14
}