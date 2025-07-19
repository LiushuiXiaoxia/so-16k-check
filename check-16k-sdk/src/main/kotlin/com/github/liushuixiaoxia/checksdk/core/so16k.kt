package com.github.liushuixiaoxia.checksdk.core

import java.io.File
import java.io.RandomAccessFile

fun isElfPageAligned(file: File): Boolean {
    if (!file.exists()) return false

    RandomAccessFile(file, "r").use { raf ->
        val magic = ByteArray(4)
        raf.readFully(magic)
        if (!magic.contentEquals(byteArrayOf(0x7F, 'E'.code.toByte(), 'L'.code.toByte(), 'F'.code.toByte()))) {
            return false
        }

        val is64Bit = when (raf.readUnsignedByte()) {
            1 -> false // ELF32
            2 -> true  // ELF64
            else -> return false
        }

        val phoff: Long
        val phentsize: Int
        val phnum: Int

        if (is64Bit) {
            raf.seek(0x20); phoff = raf.readLongLE()
            raf.seek(0x36); phentsize = raf.readShortLE().toInt()
            raf.seek(0x38); phnum = raf.readShortLE().toInt()
        } else {
            raf.seek(0x1C); phoff = raf.readIntLE().toLong()
            raf.seek(0x2A); phentsize = raf.readShortLE().toInt()
            raf.seek(0x2C); phnum = raf.readShortLE().toInt()
        }

        for (i in 0 until phnum) {
            val entryOffset = phoff + i * phentsize
            raf.seek(entryOffset)

            if (is64Bit) {
                raf.skipBytes(8 + 8 + 8 + 8 + 8 + 8) // type + flags + offset + vaddr + paddr + filesz
                raf.skipBytes(8) // memsz
                val align = raf.readLongLE()
                if (!isValidAlign(align)) {
                    println("${file } ❌ align=$align 不在 2^13~2^16")
                    return false
                }
            } else {
                raf.skipBytes(4 + 4 + 4 + 4 + 4 + 4) // type + offset + vaddr + paddr + filesz + memsz
                val align = raf.readIntLE().toLong()
                if (!isValidAlign(align)) {
                    println("❌ align=$align 不在 2^13~2^16")
                    return false
                }
            }
        }

        return true
    }
}

fun isValidAlign(align: Long): Boolean {
    return align in 8192..65536 && (align and (align - 1)) == 0L
}



// Byte order helpers
fun RandomAccessFile.readShortLE(): Short =
    ((readUnsignedByte()) or (readUnsignedByte() shl 8)).toShort()

fun RandomAccessFile.readIntLE(): Int =
    readShortLE().toInt() or (readShortLE().toInt() shl 16)

fun RandomAccessFile.readLongLE(): Long =
    readIntLE().toLong() or (readIntLE().toLong() shl 32)
