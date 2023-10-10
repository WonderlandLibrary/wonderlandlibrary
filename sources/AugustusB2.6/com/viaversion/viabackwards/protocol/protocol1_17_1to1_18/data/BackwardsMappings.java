// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.data;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.Protocol1_18To1_17_1;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public final class BackwardsMappings extends com.viaversion.viabackwards.api.data.BackwardsMappings
{
    private final Int2ObjectMap<String> blockEntities;
    
    public BackwardsMappings() {
        super("1.18", "1.17", Protocol1_18To1_17_1.class, true);
        this.blockEntities = new Int2ObjectOpenHashMap<String>();
    }
    
    @Override
    protected void loadVBExtras(final JsonObject oldMappings, final JsonObject newMappings) {
        for (final Object2IntMap.Entry<String> entry : Protocol1_18To1_17_1.MAPPINGS.blockEntityIds().object2IntEntrySet()) {
            this.blockEntities.put(entry.getIntValue(), entry.getKey());
        }
    }
    
    public Int2ObjectMap<String> blockEntities() {
        return this.blockEntities;
    }
}
