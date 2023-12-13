/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.WorldPackets;
import com.viaversion.viaversion.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BlockConnectionStorage
implements StorableObject {
    private static final short[] REVERSE_BLOCK_MAPPINGS = new short[8582];
    private static Constructor<?> fastUtilLongObjectHashMap;
    private final Map<Long, Pair<byte[], NibbleArray>> blockStorage = this.createLongObjectMap();

    public void store(int x2, int y2, int z2, int blockState) {
        int mapping = REVERSE_BLOCK_MAPPINGS[blockState];
        if (mapping == -1) {
            return;
        }
        blockState = mapping;
        long pair = this.getChunkSectionIndex(x2, y2, z2);
        Pair<byte[], NibbleArray> map = this.getChunkSection(pair, (blockState & 0xF) != 0);
        short blockIndex = this.encodeBlockPos(x2, y2, z2);
        map.key()[blockIndex] = (byte)(blockState >> 4);
        NibbleArray nibbleArray = map.value();
        if (nibbleArray != null) {
            nibbleArray.set(blockIndex, blockState);
        }
    }

    public int get(int x2, int y2, int z2) {
        long pair = this.getChunkSectionIndex(x2, y2, z2);
        Pair<byte[], NibbleArray> map = this.blockStorage.get(pair);
        if (map == null) {
            return 0;
        }
        short blockPosition = this.encodeBlockPos(x2, y2, z2);
        NibbleArray nibbleArray = map.value();
        return WorldPackets.toNewId((map.key()[blockPosition] & 0xFF) << 4 | (nibbleArray == null ? 0 : (int)nibbleArray.get(blockPosition)));
    }

    public void remove(int x2, int y2, int z2) {
        long pair = this.getChunkSectionIndex(x2, y2, z2);
        Pair<byte[], NibbleArray> map = this.blockStorage.get(pair);
        if (map == null) {
            return;
        }
        short blockIndex = this.encodeBlockPos(x2, y2, z2);
        NibbleArray nibbleArray = map.value();
        if (nibbleArray != null) {
            nibbleArray.set(blockIndex, 0);
            boolean allZero = true;
            for (int i2 = 0; i2 < 4096; ++i2) {
                if (nibbleArray.get(i2) == 0) continue;
                allZero = false;
                break;
            }
            if (allZero) {
                map.setValue(null);
            }
        }
        map.key()[blockIndex] = 0;
        byte[] byArray = map.key();
        int n2 = byArray.length;
        for (int i3 = 0; i3 < n2; ++i3) {
            short entry = byArray[i3];
            if (entry == 0) continue;
            return;
        }
        this.blockStorage.remove(pair);
    }

    public void clear() {
        this.blockStorage.clear();
    }

    public void unloadChunk(int x2, int z2) {
        for (int y2 = 0; y2 < 256; y2 += 16) {
            this.blockStorage.remove(this.getChunkSectionIndex(x2 << 4, y2, z2 << 4));
        }
    }

    private Pair<byte[], NibbleArray> getChunkSection(long index, boolean requireNibbleArray) {
        Pair<byte[], NibbleArray> map = this.blockStorage.get(index);
        if (map == null) {
            map = new Pair<byte[], Object>(new byte[4096], null);
            this.blockStorage.put(index, map);
        }
        if (map.value() == null && requireNibbleArray) {
            map.setValue(new NibbleArray(4096));
        }
        return map;
    }

    private long getChunkSectionIndex(int x2, int y2, int z2) {
        return ((long)(x2 >> 4) & 0x3FFFFFFL) << 38 | ((long)(y2 >> 4) & 0xFFFL) << 26 | (long)(z2 >> 4) & 0x3FFFFFFL;
    }

    private long getChunkSectionIndex(Position position) {
        return this.getChunkSectionIndex(position.x(), position.y(), position.z());
    }

    private short encodeBlockPos(int x2, int y2, int z2) {
        return (short)((y2 & 0xF) << 8 | (x2 & 0xF) << 4 | z2 & 0xF);
    }

    private short encodeBlockPos(Position pos) {
        return this.encodeBlockPos(pos.x(), pos.y(), pos.z());
    }

    private <T> Map<Long, T> createLongObjectMap() {
        if (fastUtilLongObjectHashMap != null) {
            try {
                return (Map)fastUtilLongObjectHashMap.newInstance(new Object[0]);
            }
            catch (IllegalAccessException | InstantiationException | InvocationTargetException e2) {
                e2.printStackTrace();
            }
        }
        return new HashMap();
    }

    static {
        try {
            String className = "it" + ".unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap";
            fastUtilLongObjectHashMap = Class.forName(className).getConstructor(new Class[0]);
            Via.getPlatform().getLogger().info("Using FastUtil Long2ObjectOpenHashMap for block connections");
        }
        catch (ClassNotFoundException | NoSuchMethodException className) {
            // empty catch block
        }
        Arrays.fill(REVERSE_BLOCK_MAPPINGS, (short)-1);
        for (int i2 = 0; i2 < 4096; ++i2) {
            int newBlock = Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(i2);
            if (newBlock == -1) continue;
            BlockConnectionStorage.REVERSE_BLOCK_MAPPINGS[newBlock] = (short)i2;
        }
    }
}

