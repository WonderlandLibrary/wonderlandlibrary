// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage;

import java.util.Arrays;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Map;
import java.lang.reflect.Constructor;
import com.viaversion.viaversion.api.connection.StoredObject;

public class ChunkLightStorage extends StoredObject
{
    public static final byte[] FULL_LIGHT;
    public static final byte[] EMPTY_LIGHT;
    private static Constructor<?> fastUtilLongObjectHashMap;
    private final Map<Long, ChunkLight> storedLight;
    
    public ChunkLightStorage(final UserConnection user) {
        super(user);
        this.storedLight = this.createLongObjectMap();
    }
    
    public void setStoredLight(final byte[][] skyLight, final byte[][] blockLight, final int x, final int z) {
        this.storedLight.put(this.getChunkSectionIndex(x, z), new ChunkLight(skyLight, blockLight));
    }
    
    public ChunkLight getStoredLight(final int x, final int z) {
        return this.storedLight.get(this.getChunkSectionIndex(x, z));
    }
    
    public void clear() {
        this.storedLight.clear();
    }
    
    public void unloadChunk(final int x, final int z) {
        this.storedLight.remove(this.getChunkSectionIndex(x, z));
    }
    
    private long getChunkSectionIndex(final int x, final int z) {
        return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL);
    }
    
    private Map<Long, ChunkLight> createLongObjectMap() {
        if (ChunkLightStorage.fastUtilLongObjectHashMap != null) {
            try {
                return (Map<Long, ChunkLight>)ChunkLightStorage.fastUtilLongObjectHashMap.newInstance(new Object[0]);
            }
            catch (IllegalAccessException | InstantiationException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                e.printStackTrace();
            }
        }
        return new HashMap<Long, ChunkLight>();
    }
    
    static {
        FULL_LIGHT = new byte[2048];
        EMPTY_LIGHT = new byte[2048];
        Arrays.fill(ChunkLightStorage.FULL_LIGHT, (byte)(-1));
        Arrays.fill(ChunkLightStorage.EMPTY_LIGHT, (byte)0);
        try {
            ChunkLightStorage.fastUtilLongObjectHashMap = Class.forName("com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectOpenHashMap").getConstructor((Class<?>[])new Class[0]);
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchMethodException ex2) {}
    }
    
    public static class ChunkLight
    {
        private final byte[][] skyLight;
        private final byte[][] blockLight;
        
        public ChunkLight(final byte[][] skyLight, final byte[][] blockLight) {
            this.skyLight = skyLight;
            this.blockLight = blockLight;
        }
        
        public byte[][] getSkyLight() {
            return this.skyLight;
        }
        
        public byte[][] getBlockLight() {
            return this.blockLight;
        }
    }
}
