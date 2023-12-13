// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.data;

import java.util.Iterator;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.data.MappingDataBase;

public final class MappingData extends MappingDataBase
{
    private final Int2ObjectMap<CompoundTag> defaultChatTypes;
    
    public MappingData() {
        super("1.18", "1.19", true);
        this.defaultChatTypes = new Int2ObjectOpenHashMap<CompoundTag>();
    }
    
    @Override
    protected void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        try {
            final ListTag chatTypes = BinaryTagIO.readCompressedInputStream(MappingDataLoader.getResource("chat-types-1.19.nbt")).get("values");
            for (final Tag chatType : chatTypes) {
                final CompoundTag chatTypeCompound = (CompoundTag)chatType;
                final NumberTag idTag = chatTypeCompound.get("id");
                this.defaultChatTypes.put(idTag.asInt(), chatTypeCompound);
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public CompoundTag chatType(final int id) {
        return this.defaultChatTypes.get(id);
    }
}
