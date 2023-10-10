// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.data;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.CommandRewriter;

public class CommandRewriter1_13_1 extends CommandRewriter
{
    public CommandRewriter1_13_1(final Protocol protocol) {
        super(protocol);
        this.parserHandlers.put("minecraft:dimension", wrapper -> wrapper.write(Type.VAR_INT, 0));
    }
    
    @Override
    protected String handleArgumentType(final String argumentType) {
        if (argumentType.equals("minecraft:column_pos")) {
            return "minecraft:vec2";
        }
        if (argumentType.equals("minecraft:dimension")) {
            return "brigadier:string";
        }
        return super.handleArgumentType(argumentType);
    }
}
