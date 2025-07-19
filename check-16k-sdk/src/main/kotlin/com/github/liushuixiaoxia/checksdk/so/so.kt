package com.github.liushuixiaoxia.checksdk.so

import java.io.File
import java.io.RandomAccessFile

data class SoInfo(
    val name: String,
    val abi: SoAbiType,
    val align16K: Boolean,
    // val ignore: Boolean = abi.is32Bit(),
) {

    fun is64Bit(): Boolean {
        return abi.is64Bit()
    }

    fun is32Bit(): Boolean {
        return abi.is32Bit()
    }

    companion object {
        fun parse(so: File): SoInfo {
            val abi = SoAbiType.parse(so)
            val align16K = isElfPageAlignedV2(so)
            return SoInfo(so.name, abi, align16K)
        }
    }
}


enum class SoAbiType(val code: Int, val abi: String) {
    Unknown(0, "unknown"),
    X86(0x03, "x86"),
    X8664(0x3E, "x86_64"),
    ArmeabiV7a(0x28, "armeabi-v7a"),
    Arm64V8a(0xB7, "arm64-v8a"),
    Mips(0x08, "mips"),
    Mips64(0x0F, "mips64");

    companion object Companion {
        fun fromMachineCode(code: Int): SoAbiType {
            return entries.find { it.code == code } ?: Unknown
        }

        fun parse(file: File): SoAbiType {
            return readAbiFromSo(file.absolutePath)
        }
    }

    fun is64Bit(): Boolean {
        return this == Arm64V8a || this == X8664 || this == Mips64
    }

    fun is32Bit(): Boolean {
        return !is64Bit() && this != Unknown
    }

    override fun toString(): String = abi
}

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