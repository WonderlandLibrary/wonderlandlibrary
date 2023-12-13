// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage;

import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.Protocol1_18_2To1_19;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.Map;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class DimensionRegistryStorage implements StorableObject
{
    private final Map<String, CompoundTag> dimensions;
    private final Int2ObjectMap<CompoundTag> chatTypes;
    
    public DimensionRegistryStorage() {
        this.dimensions = new HashMap<String, CompoundTag>();
        this.chatTypes = new Int2ObjectOpenHashMap<CompoundTag>();
    }
    
    public CompoundTag dimension(final String dimensionKey) {
        final CompoundTag compoundTag = this.dimensions.get(dimensionKey);
        return (compoundTag != null) ? compoundTag.clone() : null;
    }
    
    public void addDimension(final String dimensionKey, final CompoundTag dimension) {
        this.dimensions.put(dimensionKey, dimension);
    }
    
    public CompoundTag chatType(final int id) {
        return this.chatTypes.isEmpty() ? Protocol1_18_2To1_19.MAPPINGS.chatType(id) : this.chatTypes.get(id);
    }
    
    public void addChatType(final int id, final CompoundTag chatType) {
        this.chatTypes.put(id, chatType);
    }
    
    public void clear() {
        this.dimensions.clear();
        this.chatTypes.clear();
    }
    
    @Override
    public boolean clearOnServerSwitch() {
        return false;
    }
}
