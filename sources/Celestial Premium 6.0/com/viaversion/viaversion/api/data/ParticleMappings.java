/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.gson.JsonArray;

public class ParticleMappings {
    private final Mappings mappings;
    private final int blockId;
    private final int fallingDustId;
    private final int itemId;

    public ParticleMappings(JsonArray oldMappings, Mappings mappings) {
        this.mappings = mappings;
        Object2IntMap<String> map = MappingDataLoader.arrayToMap(oldMappings);
        this.blockId = map.getInt("block");
        this.fallingDustId = map.getInt("falling_dust");
        this.itemId = map.getInt("item");
    }

    public Mappings getMappings() {
        return this.mappings;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public int getFallingDustId() {
        return this.fallingDustId;
    }

    public int getItemId() {
        return this.itemId;
    }
}

