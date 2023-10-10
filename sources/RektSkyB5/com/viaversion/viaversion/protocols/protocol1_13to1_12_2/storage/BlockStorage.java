/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.flare.SyncMap;
import java.util.Map;

public class BlockStorage
implements StorableObject {
    private static final IntSet WHITELIST;
    private final Map<Position, ReplacementData> blocks = SyncMap.hashmap();

    public void store(Position position, int block) {
        this.store(position, block, -1);
    }

    public void store(Position position, int block, int replacementId) {
        if (!WHITELIST.contains(block)) {
            return;
        }
        this.blocks.put(position, new ReplacementData(block, replacementId));
    }

    public boolean isWelcome(int block) {
        return WHITELIST.contains(block);
    }

    public boolean contains(Position position) {
        return this.blocks.containsKey(position);
    }

    public ReplacementData get(Position position) {
        return this.blocks.get(position);
    }

    public ReplacementData remove(Position position) {
        return this.blocks.remove(position);
    }

    static {
        int i2;
        WHITELIST = new IntOpenHashSet(46, 0.99f);
        WHITELIST.add(5266);
        for (i2 = 0; i2 < 16; ++i2) {
            WHITELIST.add(972 + i2);
        }
        for (i2 = 0; i2 < 20; ++i2) {
            WHITELIST.add(6854 + i2);
        }
        for (i2 = 0; i2 < 4; ++i2) {
            WHITELIST.add(7110 + i2);
        }
        for (i2 = 0; i2 < 5; ++i2) {
            WHITELIST.add(5447 + i2);
        }
    }

    public static final class ReplacementData {
        private final int original;
        private int replacement;

        public ReplacementData(int original, int replacement) {
            this.original = original;
            this.replacement = replacement;
        }

        public int getOriginal() {
            return this.original;
        }

        public int getReplacement() {
            return this.replacement;
        }

        public void setReplacement(int replacement) {
            this.replacement = replacement;
        }
    }
}

