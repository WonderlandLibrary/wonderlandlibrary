// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.type.types.version.Types1_17;
import com.viaversion.viaversion.api.type.types.version.Types1_18;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.Protocol1_17_1To1_18;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_18 extends EntityRewriter<Protocol1_17_1To1_18>
{
    public EntityPackets1_18(final Protocol1_17_1To1_18 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        this.registerMetadataRewriter(ClientboundPackets1_18.ENTITY_METADATA, Types1_18.METADATA_LIST, Types1_17.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.read(Type.VAR_INT);
                this.handler(EntityPackets1_18.this.worldDataTrackerHandler(1));
                this.handler(wrapper -> {
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final CompoundTag biomeRegistry = registry.get("minecraft:worldgen/biome");
                    final ListTag biomes = biomeRegistry.get("value");
                    biomes.getValue().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag biome = iterator.next();
                        final CompoundTag biomeCompound = ((CompoundTag)biome).get("element");
                        final StringTag category = biomeCompound.get("category");
                        if (category.getValue().equals("mountain")) {
                            category.setValue("extreme_hills");
                        }
                        biomeCompound.put("depth", new FloatTag(0.125f));
                        biomeCompound.put("scale", new FloatTag(0.05f));
                    }
                    EntityPackets1_18.this.tracker(wrapper.user()).setBiomesSent(biomes.size());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.handler(EntityPackets1_18.this.worldDataTrackerHandler(0));
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().handler((event, meta) -> {
            meta.setMetaType(Types1_17.META_TYPES.byId(meta.metaType().typeId()));
            final MetaType type = meta.metaType();
            if (type == Types1_17.META_TYPES.particleType) {
                final Particle particle = (Particle)meta.getValue();
                if (particle.getId() == 3) {
                    final Particle.ParticleData data = particle.getArguments().remove(0);
                    final int blockState = (int)data.getValue();
                    if (blockState == 7786) {
                        particle.setId(3);
                    }
                    else {
                        particle.setId(2);
                    }
                }
                else {
                    this.rewriteParticle(particle);
                }
            }
            return;
        });
        this.registerMetaTypeHandler(Types1_17.META_TYPES.itemType, null, null, Types1_17.META_TYPES.optionalComponentType);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_17Types.getTypeFromId(typeId);
    }
}
