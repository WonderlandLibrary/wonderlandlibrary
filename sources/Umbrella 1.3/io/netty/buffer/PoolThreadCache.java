/*
 * Decompiled with CFR 0.150.
 */
package io.netty.buffer;

import io.netty.buffer.PoolArena;
import java.nio.ByteBuffer;

final class PoolThreadCache {
    final PoolArena<byte[]> heapArena;
    final PoolArena<ByteBuffer> directArena;

    PoolThreadCache(PoolArena<byte[]> heapArena, PoolArena<ByteBuffer> directArena) {
        this.heapArena = heapArena;
        this.directArena = directArena;
    }
}

