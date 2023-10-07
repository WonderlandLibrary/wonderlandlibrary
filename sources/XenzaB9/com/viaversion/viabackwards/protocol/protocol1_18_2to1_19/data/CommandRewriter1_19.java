// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.CommandRewriter;

public final class CommandRewriter1_19 extends CommandRewriter
{
    public CommandRewriter1_19(final Protocol protocol) {
        super(protocol);
        this.parserHandlers.put("minecraft:template_mirror", wrapper -> wrapper.write(Type.VAR_INT, 0));
        this.parserHandlers.put("minecraft:template_rotation", wrapper -> wrapper.write(Type.VAR_INT, 0));
    }
}
