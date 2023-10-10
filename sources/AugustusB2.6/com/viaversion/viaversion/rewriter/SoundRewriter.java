// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;

public class SoundRewriter
{
    protected final Protocol protocol;
    protected final IdRewriteFunction idRewriter;
    
    public SoundRewriter(final Protocol protocol) {
        this.protocol = protocol;
        this.idRewriter = (id -> protocol.getMappingData().getSoundMappings().getNewId(id));
    }
    
    public SoundRewriter(final Protocol protocol, final IdRewriteFunction idRewriter) {
        this.protocol = protocol;
        this.idRewriter = idRewriter;
    }
    
    public void registerSound(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(SoundRewriter.this.getSoundHandler());
            }
        });
    }
    
    public PacketHandler getSoundHandler() {
        final int soundId;
        final int mappedId;
        return wrapper -> {
            soundId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            mappedId = this.idRewriter.rewrite(soundId);
            if (mappedId == -1) {
                wrapper.cancel();
            }
            else if (soundId != mappedId) {
                wrapper.set(Type.VAR_INT, 0, mappedId);
            }
        };
    }
}
