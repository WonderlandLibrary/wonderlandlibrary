// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import java.util.Arrays;
import com.viaversion.viaversion.api.Via;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.WorldPackets;
import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;
import com.viaversion.viaversion.util.Pair;
import java.util.Map;
import java.lang.reflect.Constructor;
import com.viaversion.viaversion.api.connection.StorableObject;

public class BlockConnectionStorage implements StorableObject
{
    private static final short[] REVERSE_BLOCK_MAPPINGS;
    private static Constructor<?> fastUtilLongObjectHashMap;
    private final Map<Long, Pair<byte[], NibbleArray>> blockStorage;
    
    public BlockConnectionStorage() {
        this.blockStorage = this.createLongObjectMap();
    }
    
    public void store(final int x, final int y, final int z, int blockState) {
        final short mapping = BlockConnectionStorage.REVERSE_BLOCK_MAPPINGS[blockState];
        if (mapping == -1) {
            return;
        }
        blockState = mapping;
        final long pair = this.getChunkSectionIndex(x, y, z);
        final Pair<byte[], NibbleArray> map = this.getChunkSection(pair, (blockState & 0xF) != 0x0);
        final int blockIndex = this.encodeBlockPos(x, y, z);
        map.key()[blockIndex] = (byte)(blockState >> 4);
        final NibbleArray nibbleArray = map.value();
        if (nibbleArray != null) {
            nibbleArray.set(blockIndex, blockState);
        }
    }
    
    public int get(final int x, final int y, final int z) {
        final long pair = this.getChunkSectionIndex(x, y, z);
        final Pair<byte[], NibbleArray> map = this.blockStorage.get(pair);
        if (map == null) {
            return 0;
        }
        final short blockPosition = this.encodeBlockPos(x, y, z);
        final NibbleArray nibbleArray = map.value();
        return WorldPackets.toNewId((map.key()[blockPosition] & 0xFF) << 4 | ((nibbleArray == null) ? 0 : nibbleArray.get(blockPosition)));
    }
    
    public void remove(final int x, final int y, final int z) {
        final long pair = this.getChunkSectionIndex(x, y, z);
        final Pair<byte[], NibbleArray> map = this.blockStorage.get(pair);
        if (map == null) {
            return;
        }
        final int blockIndex = this.encodeBlockPos(x, y, z);
        final NibbleArray nibbleArray = map.value();
        if (nibbleArray != null) {
            nibbleArray.set(blockIndex, 0);
            boolean allZero = true;
            for (int i = 0; i < 4096; ++i) {
                if (nibbleArray.get(i) != 0) {
                    allZero = false;
                    break;
                }
            }
            if (allZero) {
                map.setValue(null);
            }
        }
        map.key()[blockIndex] = 0;
        for (final short entry : map.key()) {
            if (entry != 0) {
                return;
            }
        }
        this.blockStorage.remove(pair);
    }
    
    public void clear() {
        this.blockStorage.clear();
    }
    
    public void unloadChunk(final int x, final int z) {
        for (int y = 0; y < 256; y += 16) {
            this.blockStorage.remove(this.getChunkSectionIndex(x << 4, y, z << 4));
        }
    }
    
    private Pair<byte[], NibbleArray> getChunkSection(final long index, final boolean requireNibbleArray) {
        Pair<byte[], NibbleArray> map = this.blockStorage.get(index);
        if (map == null) {
            map = new Pair<byte[], NibbleArray>(new byte[4096], null);
            this.blockStorage.put(index, map);
        }
        if (map.value() == null && requireNibbleArray) {
            map.setValue(new NibbleArray(4096));
        }
        return map;
    }
    
    private long getChunkSectionIndex(final int x, final int y, final int z) {
        return ((long)(x >> 4) & 0x3FFFFFFL) << 38 | ((long)(y >> 4) & 0xFFFL) << 26 | ((long)(z >> 4) & 0x3FFFFFFL);
    }
    
    private long getChunkSectionIndex(final Position position) {
        return this.getChunkSectionIndex(position.x(), position.y(), position.z());
    }
    
    private short encodeBlockPos(final int x, final int y, final int z) {
        return (short)((y & 0xF) << 8 | (x & 0xF) << 4 | (z & 0xF));
    }
    
    private short encodeBlockPos(final Position pos) {
        return this.encodeBlockPos(pos.x(), pos.y(), pos.z());
    }
    
    private <T> Map<Long, T> createLongObjectMap() {
        if (BlockConnectionStorage.fastUtilLongObjectHashMap != null) {
            try {
                return (Map<Long, T>)BlockConnectionStorage.fastUtilLongObjectHashMap.newInstance(new Object[0]);
            }
            catch (IllegalAccessException | InstantiationException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                e.printStackTrace();
            }
        }
        return new HashMap<Long, T>();
    }
    
    static {
        REVERSE_BLOCK_MAPPINGS = new short[8582];
        try {
            final String className = "it" + ".unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap";
            BlockConnectionStorage.fastUtilLongObjectHashMap = Class.forName(className).getConstructor((Class<?>[])new Class[0]);
            Via.getPlatform().getLogger().info("Using FastUtil Long2ObjectOpenHashMap for block connections");
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchMethodException ex2) {}
        Arrays.fill(BlockConnectionStorage.REVERSE_BLOCK_MAPPINGS, (short)(-1));
        for (int i = 0; i < 4096; ++i) {
            final int newBlock = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(i);
            if (newBlock != -1) {
                BlockConnectionStorage.REVERSE_BLOCK_MAPPINGS[newBlock] = (short)i;
            }
        }
    }
}
