// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class SoundRewriter extends com.viaversion.viaversion.rewriter.SoundRewriter
{
    private final BackwardsProtocol protocol;
    
    public SoundRewriter(final BackwardsProtocol protocol) {
        super(protocol);
        this.protocol = protocol;
    }
    
    public void registerNamedSound(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(SoundRewriter.this.getNamedSoundHandler());
            }
        });
    }
    
    public void registerStopSound(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(SoundRewriter.this.getStopSoundHandler());
            }
        });
    }
    
    public PacketHandler getNamedSoundHandler() {
        final String soundId;
        final String mappedId;
        return wrapper -> {
            soundId = wrapper.get(Type.STRING, 0);
            mappedId = this.protocol.getMappingData().getMappedNamedSound(soundId);
            if (mappedId != null) {
                if (!mappedId.isEmpty()) {
                    wrapper.set(Type.STRING, 0, mappedId);
                }
                else {
                    wrapper.cancel();
                }
            }
        };
    }
    
    public PacketHandler getStopSoundHandler() {
        final byte flags;
        String soundId;
        String mappedId;
        return wrapper -> {
            flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
            if ((flags & 0x2) != 0x0) {
                if ((flags & 0x1) != 0x0) {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                }
                soundId = wrapper.read(Type.STRING);
                mappedId = this.protocol.getMappingData().getMappedNamedSound(soundId);
                if (mappedId == null) {
                    wrapper.write(Type.STRING, soundId);
                }
                else if (!mappedId.isEmpty()) {
                    wrapper.write(Type.STRING, mappedId);
                }
                else {
                    wrapper.cancel();
                }
            }
        };
    }
}
