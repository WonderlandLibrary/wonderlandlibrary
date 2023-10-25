/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.minecraft.chunks;

import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.ArrayList;
import java.util.List;

public class Chunk1_8
extends BaseChunk {
    private boolean unloadPacket;

    public Chunk1_8(int x, int z, boolean groundUp, int bitmask, ChunkSection[] sections, int[] biomeData, List<CompoundTag> blockEntities) {
        super(x, z, groundUp, false, bitmask, sections, biomeData, blockEntities);
    }

    public Chunk1_8(int x, int z) {
        this(x, z, true, 0, new ChunkSection[16], null, new ArrayList<CompoundTag>());
        this.unloadPacket = true;
    }

    public boolean hasBiomeData() {
        return this.biomeData != null && this.fullChunk;
    }

    public boolean isUnloadPacket() {
        return this.unloadPacket;
    }
}

