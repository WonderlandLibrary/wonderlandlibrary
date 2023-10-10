// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.blockentities;

import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.gson.JsonParser;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class CommandBlockHandler implements BlockEntityProvider.BlockEntityHandler
{
    private final Protocol1_13To1_12_2 protocol;
    
    public CommandBlockHandler() {
        this.protocol = Via.getManager().getProtocolManager().getProtocol(Protocol1_13To1_12_2.class);
    }
    
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        final Tag name = tag.get("CustomName");
        if (name instanceof StringTag) {
            ((StringTag)name).setValue(ChatRewriter.legacyTextToJsonString(((StringTag)name).getValue()));
        }
        final Tag out = tag.get("LastOutput");
        if (out instanceof StringTag) {
            final JsonElement value = JsonParser.parseString(((StringTag)out).getValue());
            this.protocol.getComponentRewriter().processText(value);
            ((StringTag)out).setValue(value.toString());
        }
        return -1;
    }
}
