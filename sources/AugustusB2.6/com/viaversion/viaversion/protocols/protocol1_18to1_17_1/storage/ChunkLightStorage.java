// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_18to1_17_1.storage;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class ChunkLightStorage implements StorableObject
{
    private final Map<Long, ChunkLight> lightPackets;
    private final Set<Long> loadedChunks;
    
    public ChunkLightStorage() {
        this.lightPackets = new HashMap<Long, ChunkLight>();
        this.loadedChunks = new HashSet<Long>();
    }
    
    public void storeLight(final int x, final int z, final ChunkLight chunkLight) {
        this.lightPackets.put(this.getChunkSectionIndex(x, z), chunkLight);
    }
    
    public ChunkLight removeLight(final int x, final int z) {
        return this.lightPackets.remove(this.getChunkSectionIndex(x, z));
    }
    
    public ChunkLight getLight(final int x, final int z) {
        return this.lightPackets.get(this.getChunkSectionIndex(x, z));
    }
    
    public boolean addLoadedChunk(final int x, final int z) {
        return this.loadedChunks.add(this.getChunkSectionIndex(x, z));
    }
    
    public boolean isLoaded(final int x, final int z) {
        return this.loadedChunks.contains(this.getChunkSectionIndex(x, z));
    }
    
    public void clear(final int x, final int z) {
        final long index = this.getChunkSectionIndex(x, z);
        this.lightPackets.remove(index);
        this.loadedChunks.remove(index);
    }
    
    public void clear() {
        this.loadedChunks.clear();
        this.lightPackets.clear();
    }
    
    private long getChunkSectionIndex(final int x, final int z) {
        return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL);
    }
    
    public static final class ChunkLight
    {
        private final boolean trustEdges;
        private final long[] skyLightMask;
        private final long[] blockLightMask;
        private final long[] emptySkyLightMask;
        private final long[] emptyBlockLightMask;
        private final byte[][] skyLight;
        private final byte[][] blockLight;
        
        public ChunkLight(final boolean trustEdges, final long[] skyLightMask, final long[] blockLightMask, final long[] emptySkyLightMask, final long[] emptyBlockLightMask, final byte[][] skyLight, final byte[][] blockLight) {
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
