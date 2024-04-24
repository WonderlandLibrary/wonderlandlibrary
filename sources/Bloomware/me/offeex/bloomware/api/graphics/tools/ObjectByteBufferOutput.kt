package me.offeex.bloomware.api.graphics.tools

import java.io.ObjectOutput
import java.nio.ByteBuffer

class ObjectByteBufferOutput(val buffer : ByteBuffer): ObjectOutput {
    override fun write(b: Int) {
        buffer.put(b.toByte())
    }

    override fun write(b: ByteArray) {
        buffer.put(b)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        buffer.put(b, off, len)
    }

    override fun writeBoolean(v: Boolean) {
        buffer.put(if (v) 1 else 0)
    }

    override fun writeByte(v: Int) {
        buffer.put(v.toByte())
    }

    override fun writeShort(v: Int) {
        buffer.putShort(v.toShort())
    }

    override fun writeChar(v: Int) {
        buffer.putChar(v.toChar())
    }

    override fun writeInt(v: Int) {
        buffer.putInt(v)
    }

    override fun writeLong(v: Long) {
        buffer.putLong(v)
    }

    override fun writeFloat(v: Float) {
        buffer.putFloat(v)
    }

    override fun writeDouble(v: Double) {
        buffer.putDouble(v)
    }

    override fun writeBytes(s: String) {
        buffer.put(s.toByteArray())
    }

    override fun writeChars(s: String) {
        buffer.put(s.toByteArray())
    }

    override fun writeUTF(s: String) {
        buffer.put(s.toByteArray())
    }

    override fun close() {
        // Nothing to do
    }

    override fun writeObject(obj: Any?) {
        throw IllegalArgumentException("Unsupported attribute type")
    }

    override fun flush() {
        // Nothing to do
    }
}