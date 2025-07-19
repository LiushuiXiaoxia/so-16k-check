package com.github.liushuixiaoxia.checksdk.core


import java.io.File
import java.io.RandomAccessFile
import java.util.zip.ZipFile

/**
 * Check .so file alignment in the given APK file.
 *
 * @param apkFile The APK file to check.
 * @param alignment The byte alignment to check against (default is 16384 = 16K).
 * @return Map of entry name to (offset, isAligned)
 */
fun checkSoAlignment(apkFile: File, alignment: Int = 16 * 1024): Map<String, Pair<Long, Boolean>> {
    require(apkFile.exists()) { "APK file does not exist: ${apkFile.absolutePath}" }
    require(apkFile.extension == "apk") { "File is not an APK: ${apkFile.name}" }

    val result = mutableMapOf<String, Pair<Long, Boolean>>()

    ZipFile(apkFile).use { zip ->
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.name.endsWith(".so")) {
                val offset = getLocalHeaderOffset(apkFile, entry.name)
                    ?: 0L
                    // ?: throw IllegalStateException("Unable to get offset for entry: ${entry.name}")

                val aligned = offset % alignment == 0L
                result[entry.name] = offset to aligned
            }
        }
    }

    return result
}

/**
 * Retrieve the local header offset of a given entry in the ZIP file.
 *
 * @param zipFile The ZIP (APK) file.
 * @param entryName The name of the entry to find.
 * @return The offset of the local file header for the entry, or null if not found.
 */
fun getLocalHeaderOffset(zipFile: File, entryName: String): Long? {
    RandomAccessFile(zipFile, "r").use { raf ->
        val fileLength = raf.length()
        val maxCommentLength = 65535
        val searchLength = minOf(fileLength, (22L + maxCommentLength)).toInt()
        val buffer = ByteArray(searchLength)

        raf.seek(fileLength - searchLength)
        raf.readFully(buffer)

        for (i in searchLength - 22 downTo 0) {
            if (buffer[i].toInt() == 0x50 &&
                buffer[i + 1].toInt() == 0x4b &&
                buffer[i + 2].toInt() == 0x05 &&
                buffer[i + 3].toInt() == 0x06) {
                val centralDirOffset = buffer.copyOfRange(i + 16, i + 20).reversedArray()
                    .fold(0L) { acc, byte -> (acc shl 8) or (byte.toLong() and 0xFF) }

                raf.seek(centralDirOffset)
                while (raf.filePointer < fileLength) {
                    val sig = raf.readInt()
                    if (sig != 0x02014b50) break // central directory signature

                    raf.skipBytes(24)
                    val fileNameLen = raf.readUnsignedShort()
                    val extraLen = raf.readUnsignedShort()
                    val commentLen = raf.readUnsignedShort()
                    raf.skipBytes(8)
                    val localHeaderOffset = raf.readInt().toLong() and 0xFFFFFFFF

                    val fileNameBytes = ByteArray(fileNameLen)
                    raf.readFully(fileNameBytes)
                    val name = String(fileNameBytes)

                    if (name == entryName) {
                        return localHeaderOffset
                    }

                    raf.skipBytes(extraLen + commentLen)
                }
            }
        }
    }

    return null
}
