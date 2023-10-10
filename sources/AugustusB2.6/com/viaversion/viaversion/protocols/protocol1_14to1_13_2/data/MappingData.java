// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data;

import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.Map;
import java.util.HashMap;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class MappingData extends MappingDataBase
{
    private IntSet motionBlocking;
    private IntSet nonFullBlocks;
    
    public MappingData() {
        super("1.13.2", "1.14");
    }
    
    public void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        final JsonObject blockStates = newMappings.getAsJsonObject("blockstates");
        final Map<String, Integer> blockStateMap = new HashMap<String, Integer>(blockStates.entrySet().size());
        for (final Map.Entry<String, JsonElement> entry : blockStates.entrySet()) {
            blockStateMap.put(entry.getValue().getAsString(), Integer.parseInt(entry.getKey()));
        }
        final JsonObject heightMapData = MappingDataLoader.loadData("heightMapData-1.14.json");
        final JsonArray motionBlocking = heightMapData.getAsJsonArray("MOTION_BLOCKING");
        this.motionBlocking = new IntOpenHashSet(motionBlocking.size(), 0.99f);
        for (final JsonElement blockState : motionBlocking) {
            final String key = blockState.getAsString();
            final Integer id = blockStateMap.get(key);
            if (id == null) {
                Via.getPlatform().getLogger().warning("Unknown blockstate " + key + " :(");
            }
            else {
                this.motionBlocking.add((int)id);
            }
        }
        if (Via.getConfig().isNonFullBlockLightFix()) {
            this.nonFullBlocks = new IntOpenHashSet(1611, 0.99f);
            for (final Map.Entry<String, JsonElement> blockstates : oldMappings.getAsJsonObject("blockstates").entrySet()) {
                final String state = blockstates.getValue().getAsString();
                if (state.contains("_slab") || state.contains("_stairs") || state.contains("_wall[")) {
                    this.nonFullBlocks.add(this.blockStateMappings.getNewId(Integer.parseInt(blockstates.getKey())));
                }
            }
            this.nonFullBlocks.add(this.blockStateMappings.getNewId(8163));
            for (int i = 3060; i <= 3067; ++i) {
                this.nonFullBlocks.add(this.blockStateMappings.getNewId(i));
            }
        }
    }
    
    public IntSet getMotionBlocking() {
        return this.motionBlocking;
    }
    
    public IntSet getNonFullBlocks() {
        return this.nonFullBlocks;
    }
}
