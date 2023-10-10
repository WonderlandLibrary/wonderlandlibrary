// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.NamedSoundMapping;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class SoundPackets1_13 extends RewriterBase<Protocol1_12_2To1_13>
{
    private static final String[] SOUND_SOURCES;
    
    public SoundPackets1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String newSound;
                String oldSound;
                this.handler(wrapper -> {
                    newSound = wrapper.get(Type.STRING, 0);
                    oldSound = NamedSoundMapping.getOldId(newSound);
                    if (oldSound != null || (oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getMappedNamedSound(newSound)) != null) {
                        wrapper.set(Type.STRING, 0, oldSound);
                    }
                    else if (!Via.getConfig().isSuppressConversionWarnings()) {
                        ViaBackwards.getPlatform().getLogger().warning("Unknown named sound in 1.13->1.12 protocol: " + newSound);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, ClientboundPackets1_12_1, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.STOP_SOUND, ClientboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                final byte flags;
                String source;
                String newSound;
                String sound;
                this.handler(wrapper -> {
                    wrapper.write(Type.STRING, "MC|StopSound");
                    flags = wrapper.read((Type<Byte>)Type.BYTE);
                    if ((flags & 0x1) != 0x0) {
                        source = SoundPackets1_13.SOUND_SOURCES[wrapper.read((Type<Integer>)Type.VAR_INT)];
                    }
                    else {
                        source = "";
                    }
                    if ((flags & 0x2) != 0x0) {
                        newSound = wrapper.read(Type.STRING);
                        sound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getMappedNamedSound(newSound);
                        if (sound == null) {
                            sound = "";
                        }
                    }
                    else {
                        sound = "";
                    }
                    wrapper.write(Type.STRING, source);
                    wrapper.write(Type.STRING, sound);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int newSound;
                final int oldSound;
                this.handler(wrapper -> {
                    newSound = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getSoundMappings().getNewId(newSound);
                    if (oldSound == -1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.set(Type.VAR_INT, 0, oldSound);
                    }
                });
            }
        });
    }
    
    static {
        SOUND_SOURCES = new String[] { "master", "music", "record", "weather", "block", "hostile", "neutral", "player", "ambient", "voice" };
    }
}
