// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_18_2to1_18;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_18_2To1_18 extends AbstractProtocol<ClientboundPackets1_18, ClientboundPackets1_18, ServerboundPackets1_17, ServerboundPackets1_17>
{
    public Protocol1_18_2To1_18() {
        super(ClientboundPackets1_18.class, ClientboundPackets1_18.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
    }
    
    @Override
    protected void registerPackets() {
        final TagRewriter tagRewriter = new TagRewriter(this);
        tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:fall_damage_resetting");
        tagRewriter.registerGeneric(ClientboundPackets1_18.TAGS);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_18.ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE, Type.VAR_INT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_18.REMOVE_ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE, Type.VAR_INT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_18.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final CompoundTag dimensionsHolder = registry.get("minecraft:dimension_type");
                    final ListTag dimensions = dimensionsHolder.get("value");
                    dimensions.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag dimension = iterator.next();
                        Protocol1_18_2To1_18.this.addTagPrefix(((CompoundTag)dimension).get("element"));
                    }
                    Protocol1_18_2To1_18.this.addTagPrefix(wrapper.get(Type.NBT, 1));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_18.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> Protocol1_18_2To1_18.this.addTagPrefix(wrapper.passthrough(Type.NBT)));
            }
        });
    }
    
    private void addTagPrefix(final CompoundTag tag) {
        final Tag infiniburnTag = tag.get("infiniburn");
        if (infiniburnTag instanceof StringTag) {
            final StringTag infiniburn = (StringTag)infiniburnTag;
            infiniburn.setValue("#" + infiniburn.getValue());
        }
    }
}
