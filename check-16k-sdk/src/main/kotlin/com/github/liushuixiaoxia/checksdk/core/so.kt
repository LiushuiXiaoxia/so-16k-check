package com.github.liushuixiaoxia.checksdk.core

import com.github.liushuixiaoxia.checksdk.SoAbiType
import java.io.File
import java.io.RandomAccessFile


fun readAbiFromSo(filePath: String): SoAbiType {
    val file = File(filePath)
    if (!file.exists()) return SoAbiType.Unknown

    RandomAccessFile(file, "r").use { raf ->
        val magic = ByteArray(4)
        raf.readFully(magic)
        if (!magic.contentEquals(byteArrayOf(0x7F, 'E'.code.toByte(), 'L'.code.toByte(), 'F'.code.toByte()))) {
            return SoAbiType.Unknown
        }

        val eiClass = raf.readUnsignedByte() // 1=ELF32, 2=ELF64（不一定需要用）
        raf.seek(0x12)
        val eMachineBytes = ByteArray(2)
        raf.readFully(eMachineBytes)
        val eMachine = (eMachineBytes[1].toInt() and 0xFF shl 8) or (eMachineBytes[0].toInt() and 0xFF)

        return SoAbiType.fromMachineCode(eMachine)
    }
}