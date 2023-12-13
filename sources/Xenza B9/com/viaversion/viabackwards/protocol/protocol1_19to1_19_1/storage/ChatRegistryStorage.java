// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.storage;

import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class ChatRegistryStorage implements StorableObject
{
    private final Int2ObjectMap<CompoundTag> chatTypes;
    
    public ChatRegistryStorage() {
        this.chatTypes = new Int2ObjectOpenHashMap<CompoundTag>();
    }
    
    public CompoundTag chatType(final int id) {
        return this.chatTypes.isEmpty() ? Protocol1_19To1_18_2.MAPPINGS.chatType(id) : this.chatTypes.get(id);
    }
    
    public void addChatType(final int id, final CompoundTag chatType) {
        this.chatTypes.put(id, chatType);
    }
    
    public void clear() {
        this.chatTypes.clear();
    }
    
    @Override
    public boolean clearOnServerSwitch() {
        return false;
    }
}
