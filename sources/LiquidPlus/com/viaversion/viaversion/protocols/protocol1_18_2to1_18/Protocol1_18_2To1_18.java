/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_18_2to1_18;

import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.rewriter.TagRewriter;

public final class Protocol1_18_2To1_18
extends AbstractProtocol<ClientboundPackets1_18, ClientboundPackets1_18, ServerboundPackets1_17, ServerboundPackets1_17> {
    public Protocol1_18_2To1_18() {
        super(ClientboundPackets1_18.class, ClientboundPackets1_18.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
    }

    @Override
    protected void registerPackets() {
        TagRewriter tagRewriter = new TagRewriter(this);
        tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:fall_damage_resetting");
        tagRewriter.registerGeneric(ClientboundPackets1_18.TAGS);
        this.registerClientbound(ClientboundPackets1_18.ENTITY_EFFECT, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map((Type)Type.BYTE, Type.VAR_INT);
            }
        });
        this.registerClientbound(ClientboundPackets1_18.REMOVE_ENTITY_EFFECT, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map((Type)Type.BYTE, Type.VAR_INT);
            }
        });
        this.registerClientbound(ClientboundPackets1_18.JOIN_GAME, new PacketRemapper(){

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
                    CompoundTag registry = wrapper.get(Type.NBT, 0);
                    CompoundTag dimensionsHolder = (CompoundTag)registry.get("minecraft:dimension_type");
                    ListTag dimensions = (ListTag)dimensionsHolder.get("value");
                    for (Tag dimension : dimensions) {
                        Protocol1_18_2To1_18.this.addTagPrefix((CompoundTag)((CompoundTag)dimension).get("element"));
                    }
                    Protocol1_18_2To1_18.this.addTagPrefix(wrapper.get(Type.NBT, 1));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_18.RESPAWN, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> Protocol1_18_2To1_18.this.addTagPrefix(wrapper.passthrough(Type.NBT)));
            }
        });
    }

    private void addTagPrefix(CompoundTag tag) {
        Object infiniburnTag = tag.get("infiniburn");
        if (infiniburnTag instanceof StringTag) {
            StringTag infiniburn = (StringTag)infiniburnTag;
            infiniburn.setValue("#" + infiniburn.getValue());
        }
    }
}

