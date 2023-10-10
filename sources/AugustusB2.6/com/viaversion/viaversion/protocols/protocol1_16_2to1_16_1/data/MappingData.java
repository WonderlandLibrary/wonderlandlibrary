// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.data;

import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import java.io.IOException;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.HashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.Map;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class MappingData extends MappingDataBase
{
    private final Map<String, CompoundTag> dimensionDataMap;
    private CompoundTag dimensionRegistry;
    
    public MappingData() {
        super("1.16", "1.16.2", true);
        this.dimensionDataMap = new HashMap<String, CompoundTag>();
    }
    
    public void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        try {
            this.dimensionRegistry = BinaryTagIO.readCompressedInputStream(MappingDataLoader.getResource("dimension-registry-1.16.2.nbt"));
        }
        catch (IOException e) {
            Via.getPlatform().getLogger().severe("Error loading dimension registry:");
            e.printStackTrace();
        }
        final ListTag dimensions = this.dimensionRegistry.get("minecraft:dimension_type").get("value");
        for (final Tag dimension : dimensions) {
            final CompoundTag dimensionCompound = (CompoundTag)dimension;
            final CompoundTag dimensionData = new CompoundTag(dimensionCompound.get("element").getValue());
            this.dimensionDataMap.put(dimensionCompound.get("name").getValue(), dimensionData);
        }
    }
    
    public Map<String, CompoundTag> getDimensionDataMap() {
        return this.dimensionDataMap;
    }
    
    public CompoundTag getDimensionRegistry() {
        return this.dimensionRegistry;
    }
}
