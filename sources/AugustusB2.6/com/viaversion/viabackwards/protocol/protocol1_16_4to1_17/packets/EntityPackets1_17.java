// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16_2Types;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.Particle;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.type.types.version.Types1_17;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.Protocol1_16_4To1_17;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_17 extends EntityRewriter<Protocol1_16_4To1_17>
{
    public EntityPackets1_17(final Protocol1_16_4To1_17 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        this.registerTrackerWithData(ClientboundPackets1_17.SPAWN_ENTITY, Entity1_17Types.FALLING_BLOCK);
        this.registerSpawnTracker(ClientboundPackets1_17.SPAWN_MOB);
        this.registerTracker(ClientboundPackets1_17.SPAWN_EXPERIENCE_ORB, Entity1_17Types.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_17.SPAWN_PAINTING, Entity1_17Types.PAINTING);
        this.registerTracker(ClientboundPackets1_17.SPAWN_PLAYER, Entity1_17Types.PLAYER);
        this.registerMetadataRewriter(ClientboundPackets1_17.ENTITY_METADATA, Types1_17.METADATA_LIST, Types1_16.METADATA_LIST);
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_17.REMOVE_ENTITY, ClientboundPackets1_16_2.DESTROY_ENTITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int entityId;
                final int[] array;
                this.handler(wrapper -> {
                    entityId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    ((com.viaversion.viaversion.api.rewriter.EntityRewriter<Protocol>)EntityPackets1_17.this).tracker(wrapper.user()).removeEntity(entityId);
                    array = new int[] { entityId };
                    wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, array);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_17.JOIN_GAME, new PacketRemapper() {
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
                final byte previousGamemode;
                this.handler(wrapper -> {
                    previousGamemode = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (previousGamemode == -1) {
                        wrapper.set(Type.BYTE, 0, (Byte)0);
                    }
                    return;
                });
                this.handler(EntityRewriterBase.this.getTrackerHandler(Entity1_17Types.PLAYER, Type.INT));
                this.handler(EntityPackets1_17.this.worldDataTrackerHandler(1));
                final CompoundTag registry;
                final CompoundTag biomeRegistry;
                final ListTag biomes;
                final Iterator<Tag> iterator;
                Tag biome;
                CompoundTag biomeCompound;
                StringTag category;
                final CompoundTag dimensionRegistry;
                final ListTag dimensions;
                final Iterator<Tag> iterator2;
                Tag dimension;
                CompoundTag dimensionCompound;
                this.handler(wrapper -> {
                    registry = wrapper.get(Type.NBT, 0);
                    biomeRegistry = registry.get("minecraft:worldgen/biome");
                    biomes = biomeRegistry.get("value");
                    biomes.iterator();
                    while (iterator.hasNext()) {
                        biome = iterator.next();
                        biomeCompound = ((CompoundTag)biome).get("element");
                        category = biomeCompound.get("category");
                        if (category.getValue().equalsIgnoreCase("underground")) {
                            category.setValue("none");
                        }
                    }
                    dimensionRegistry = registry.get("minecraft:dimension_type");
                    dimensions = dimensionRegistry.get("value");
                    dimensions.iterator();
                    while (iterator2.hasNext()) {
                        dimension = iterator2.next();
                        dimensionCompound = ((CompoundTag)dimension).get("element");
                        EntityPackets1_17.this.reduceExtendedHeight(dimensionCompound, false);
                    }
                    EntityPackets1_17.this.reduceExtendedHeight(wrapper.get(Type.NBT, 1), true);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_17.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.handler(EntityPackets1_17.this.worldDataTrackerHandler(0));
                this.handler(wrapper -> EntityPackets1_17.this.reduceExtendedHeight(wrapper.get(Type.NBT, 0), true));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_17.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.read((Type<Object>)Type.BOOLEAN));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_17.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.INT, (Integer)wrapper.read((Type<T>)Type.VAR_INT)));
            }
        });
        ((Protocol1_16_4To1_17)this.protocol).mergePacket(ClientboundPackets1_17.COMBAT_ENTER, ClientboundPackets1_16_2.COMBAT_EVENT, 0);
        ((Protocol1_16_4To1_17)this.protocol).mergePacket(ClientboundPackets1_17.COMBAT_END, ClientboundPackets1_16_2.COMBAT_EVENT, 1);
        ((Protocol1_16_4To1_17)this.protocol).mergePacket(ClientboundPackets1_17.COMBAT_KILL, ClientboundPackets1_16_2.COMBAT_EVENT, 2);
    }
    
    @Override
    protected void registerRewrites() {
        final MetaType type;
        Particle particle;
        int pose;
        this.filter().handler((event, meta) -> {
            meta.setMetaType(Types1_16.META_TYPES.byId(meta.metaType().typeId()));
            type = meta.metaType();
            if (type == Types1_16.META_TYPES.particleType) {
                particle = (Particle)meta.getValue();
                if (particle.getId() == 16) {
                    particle.getArguments().subList(4, 7).clear();
                }
                else if (particle.getId() == 37) {
                    particle.setId(0);
                    particle.getArguments().clear();
                    return;
                }
                this.rewriteParticle(particle);
            }
            else if (type == Types1_16.META_TYPES.poseType) {
                pose = (int)meta.value();
                if (pose == 6) {
                    meta.setValue(1);
                }
                else if (pose > 6) {
                    meta.setValue(pose - 1);
                }
            }
            return;
        });
        this.registerMetaTypeHandler(Types1_16.META_TYPES.itemType, Types1_16.META_TYPES.blockStateType, null, Types1_16.META_TYPES.optionalComponentType);
        this.mapTypes(Entity1_17Types.values(), Entity1_16_2Types.class);
        this.filter().type(Entity1_17Types.AXOLOTL).cancel(17);
        this.filter().type(Entity1_17Types.AXOLOTL).cancel(18);
        this.filter().type(Entity1_17Types.AXOLOTL).cancel(19);
        this.filter().type(Entity1_17Types.GLOW_SQUID).cancel(16);
        this.filter().type(Entity1_17Types.GOAT).cancel(17);
        this.mapEntityTypeWithData(Entity1_17Types.AXOLOTL, Entity1_17Types.TROPICAL_FISH).jsonName();
        this.mapEntityTypeWithData(Entity1_17Types.GOAT, Entity1_17Types.SHEEP).jsonName();
        this.mapEntityTypeWithData(Entity1_17Types.GLOW_SQUID, Entity1_17Types.SQUID).jsonName();
        this.mapEntityTypeWithData(Entity1_17Types.GLOW_ITEM_FRAME, Entity1_17Types.ITEM_FRAME);
        this.filter().type(Entity1_17Types.SHULKER).addIndex(17);
        this.filter().removeIndex(7);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_17Types.getTypeFromId(typeId);
    }
    
    private void reduceExtendedHeight(final CompoundTag tag, final boolean warn) {
        final IntTag minY = tag.get("min_y");
        final IntTag height = tag.get("height");
        final IntTag logicalHeight = tag.get("logical_height");
        if (minY.asInt() != 0 || height.asInt() > 256 || logicalHeight.asInt() > 256) {
            if (warn) {
                ViaBackwards.getPlatform().getLogger().severe("Custom worlds heights are NOT SUPPORTED for 1.16 players and older and may lead to errors!");
                ViaBackwards.getPlatform().getLogger().severe("You have min/max set to " + minY.asInt() + "/" + height.asInt());
            }
            height.setValue(Math.min(256, height.asInt()));
            logicalHeight.setValue(Math.min(256, logicalHeight.asInt()));
        }
    }
}
