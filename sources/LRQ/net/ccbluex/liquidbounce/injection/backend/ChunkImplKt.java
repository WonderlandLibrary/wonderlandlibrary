/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.chunk.Chunk
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.world.IChunk;
import net.ccbluex.liquidbounce.injection.backend.ChunkImpl;
import net.minecraft.world.chunk.Chunk;

public final class ChunkImplKt {
    public static final Chunk unwrap(IChunk $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ChunkImpl)$this$unwrap).getWrapped();
    }

    public static final IChunk wrap(Chunk $this$wrap) {
        int $i$f$wrap = 0;
        return new ChunkImpl($this$wrap);
    }
}

