package me.offeex.bloomware.api.graphics.tools

import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

data class NativeBuffer(val ptr: Long, val size: Int, val buffer: ByteBuffer, val allocator: MemoryUtil.MemoryAllocator): AutoCloseable {
    companion object {
        fun createOnAllocator(size: Int, allocator: MemoryUtil.MemoryAllocator): NativeBuffer {
            val ptr = allocator.malloc(size.toLong())
            val buffer = if (ptr == 0L) {
                throw OutOfMemoryError("Failed to allocate $size bytes")
            } else {
                MemoryUtil.memByteBuffer(ptr, size)
            }
            return NativeBuffer(ptr, size, buffer, allocator)
        }
    }

    override fun close() {
        allocator.free(ptr)
    }
}