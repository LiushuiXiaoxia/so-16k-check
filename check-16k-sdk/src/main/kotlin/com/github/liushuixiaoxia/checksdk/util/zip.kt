package com.github.liushuixiaoxia.checksdk.util

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

fun unzip(zipFile: File, targetDir: File) {
    if (!targetDir.exists()) targetDir.mkdirs()

    ZipInputStream(zipFile.inputStream()).use { zip ->
        var entry = zip.nextEntry
        while (entry != null) {
            val outFile = File(targetDir, entry.name)
            if (entry.isDirectory) {
                outFile.mkdirs()
            } else {
                outFile.parentFile.mkdirs()
                FileOutputStream(outFile).use { out ->
                    zip.copyTo(out)
                }
            }
            zip.closeEntry()
            entry = zip.nextEntry
        }
    }
}
