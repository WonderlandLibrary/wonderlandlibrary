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
    public static final Chunk unwrap(IChunk iChunk) {
        boolean bl = false;
        return ((ChunkImpl)iChunk).getWrapped();
    }

    public static final IChunk wrap(Chunk chunk) {
        boolean bl = false;
        return new ChunkImpl(chunk);
    }
}

