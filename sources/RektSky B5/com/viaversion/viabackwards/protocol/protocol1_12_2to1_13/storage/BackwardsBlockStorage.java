/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BackwardsBlockStorage
implements StorableObject {
    private static final IntSet WHITELIST;
    private final Map<Position, Integer> blocks = new ConcurrentHashMap<Position, Integer>();

    public void checkAndStore(Position position, int block) {
        if (!WHITELIST.contains(block)) {
            this.blocks.remove(position);
            return;
        }
        this.blocks.put(position, block);
    }

    public boolean isWelcome(int block) {
        return WHITELIST.contains(block);
    }

    public Integer get(Position position) {
        return this.blocks.get(position);
    }

    public int remove(Position position) {
        return this.blocks.remove(position);
    }

    public void clear() {
        this.blocks.clear();
    }

    public Map<Position, Integer> getBlocks() {
        return this.blocks;
    }

    static {
        int i2;
        WHITELIST = new IntOpenHashSet(779);
        for (i2 = 5265; i2 <= 5286; ++i2) {
            WHITELIST.add(i2);
        }
        for (i2 = 0; i2 < 256; ++i2) {
            WHITELIST.add(748 + i2);
        }
        for (i2 = 6854; i2 <= 7173; ++i2) {
            WHITELIST.add(i2);
        }
        WHITELIST.add(1647);
        for (i2 = 5447; i2 <= 5566; ++i2) {
            WHITELIST.add(i2);
        }
        for (i2 = 1028; i2 <= 1039; ++i2) {
            WHITELIST.add(i2);
        }
        for (i2 = 1047; i2 <= 1082; ++i2) {
            WHITELIST.add(i2);
        }
        for (i2 = 1099; i2 <= 1110; ++i2) {
            WHITELIST.add(i2);
        }
    }
}

