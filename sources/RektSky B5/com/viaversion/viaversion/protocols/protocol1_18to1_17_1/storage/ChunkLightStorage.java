/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.protocols.protocol1_18to1_17_1.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ChunkLightStorage
implements StorableObject {
    private final Map<Long, ChunkLight> lightPackets = new HashMap<Long, ChunkLight>();
    private final Set<Long> loadedChunks = new HashSet<Long>();

    public void storeLight(int x2, int z2, ChunkLight chunkLight) {
        this.lightPackets.put(this.getChunkSectionIndex(x2, z2), chunkLight);
    }

    public @Nullable ChunkLight removeLight(int x2, int z2) {
        return this.lightPackets.remove(this.getChunkSectionIndex(x2, z2));
    }

    public @Nullable ChunkLight getLight(int x2, int z2) {
        return this.lightPackets.get(this.getChunkSectionIndex(x2, z2));
    }

    public boolean addLoadedChunk(int x2, int z2) {
        return this.loadedChunks.add(this.getChunkSectionIndex(x2, z2));
    }

    public boolean isLoaded(int x2, int z2) {
        return this.loadedChunks.contains(this.getChunkSectionIndex(x2, z2));
    }

    public void clear(int x2, int z2) {
        long index = this.getChunkSectionIndex(x2, z2);
        this.lightPackets.remove(index);
        this.loadedChunks.remove(index);
    }

    public void clear() {
        this.loadedChunks.clear();
        this.lightPackets.clear();
    }

    private long getChunkSectionIndex(int x2, int z2) {
        return ((long)x2 & 0x3FFFFFFL) << 38 | (long)z2 & 0x3FFFFFFL;
    }

    public static final class ChunkLight {
        private final boolean trustEdges;
        private final long[] skyLightMask;
        private final long[] blockLightMask;
        private final long[] emptySkyLightMask;
        private final long[] emptyBlockLightMask;
        private final byte[][] skyLight;
        private final byte[][] blockLight;

        public ChunkLight(boolean trustEdges, long[] skyLightMask, long[] blockLightMask, long[] emptySkyLightMask, long[] emptyBlockLightMask, byte[][] skyLight, byte[][] blockLight) {
            this.trustEdges = trustEdges;
            this.skyLightMask = skyLightMask;
            this.emptySkyLightMask = emptySkyLightMask;
            this.blockLightMask = blockLightMask;
            this.emptyBlockLightMask = emptyBlockLightMask;
            this.skyLight = skyLight;
            this.blockLight = blockLight;
        }

        public boolean trustEdges() {
            return this.trustEdges;
        }

        public long[] skyLightMask() {
            return this.skyLightMask;
        }

        public long[] emptySkyLightMask() {
            return this.emptySkyLightMask;
        }

        public long[] blockLightMask() {
            return this.blockLightMask;
        }

        public long[] emptyBlockLightMask() {
            return this.emptyBlockLightMask;
        }

        public byte[][] skyLight() {
            return this.skyLight;
        }

        public byte[][] blockLight() {
            return this.blockLight;
        }
    }
}

