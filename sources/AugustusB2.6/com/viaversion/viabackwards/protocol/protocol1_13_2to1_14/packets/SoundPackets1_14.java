// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class SoundPackets1_14 extends RewriterBase<Protocol1_13_2To1_14>
{
    public SoundPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final SoundRewriter soundRewriter = new SoundRewriter((BackwardsProtocol)this.protocol);
        soundRewriter.registerSound(ClientboundPackets1_14.SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_14.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_14.STOP_SOUND);
        ((Protocol<ClientboundPackets1_14, ClientboundPackets1_13, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_SOUND, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int soundId;
                final int newId;
                int category;
                int entityId;
                StoredEntityData storedEntity;
                EntityPositionStorage1_14 entityStorage;
                float volume;
                float pitch;
                int x;
                int y;
                int z;
                PacketWrapper soundPacket;
                this.handler(wrapper -> {
                    wrapper.cancel();
                    soundId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    newId = ((Protocol1_13_2To1_14)SoundPackets1_14.this.protocol).getMappingData().getSoundMappings().getNewId(soundId);
                    if (newId != -1) {
                        category = wrapper.read((Type<Integer>)Type.VAR_INT);
                        entityId = wrapper.read((Type<Integer>)Type.VAR_INT);
                        storedEntity = wrapper.user().getEntityTracker(((Protocol1_13_2To1_14)SoundPackets1_14.this.protocol).getClass()).entityData(entityId);
                        if (storedEntity == null || (entityStorage = storedEntity.get(EntityPositionStorage1_14.class)) == null) {
                            ViaBackwards.getPlatform().getLogger().warning("Untracked entity with id " + entityId);
                        }
                        else {
                            volume = wrapper.read((Type<Float>)Type.FLOAT);
                            pitch = wrapper.read((Type<Float>)Type.FLOAT);
                            x = (int)(entityStorage.getX() * 8.0);
                            y = (int)(entityStorage.getY() * 8.0);
                            z = (int)(entityStorage.getZ() * 8.0);
                            soundPacket = wrapper.create(ClientboundPackets1_13.SOUND);
                            soundPacket.write(Type.VAR_INT, newId);
                            soundPacket.write(Type.VAR_INT, category);
                            soundPacket.write(Type.INT, x);
                            soundPacket.write(Type.INT, y);
                            soundPacket.write(Type.INT, z);
                            soundPacket.write(Type.FLOAT, volume);
                            soundPacket.write(Type.FLOAT, pitch);
                            soundPacket.send(Protocol1_13_2To1_14.class);
                        }
                    }
                });
            }
        });
    }
}
