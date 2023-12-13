// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.Particle;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage.DimensionRegistryStorage;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage.StoredPainting;
import java.util.UUID;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.types.version.Types1_18;
import com.viaversion.viaversion.api.type.types.version.Types1_19;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.Protocol1_18_2To1_19;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_19 extends EntityRewriter<Protocol1_18_2To1_19>
{
    public EntityPackets1_19(final Protocol1_18_2To1_19 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        this.registerTracker(ClientboundPackets1_19.SPAWN_EXPERIENCE_ORB, Entity1_19Types.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_19.SPAWN_PLAYER, Entity1_19Types.PLAYER);
        this.registerMetadataRewriter(ClientboundPackets1_19.ENTITY_METADATA, Types1_19.METADATA_LIST, Types1_18.METADATA_LIST);
        this.registerRemoveEntities(ClientboundPackets1_19.REMOVE_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_19.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler(wrapper -> {
                    final byte headYaw = wrapper.read((Type<Byte>)Type.BYTE);
                    int data = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final EntityType entityType = EntityRewriter.this.setOldEntityId(wrapper);
                    if (entityType.isOrHasParent(Entity1_19Types.LIVINGENTITY)) {
                        wrapper.write(Type.BYTE, headYaw);
                        final byte pitch = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final byte yaw = wrapper.get((Type<Byte>)Type.BYTE, 1);
                        wrapper.set(Type.BYTE, 0, yaw);
                        wrapper.set(Type.BYTE, 1, pitch);
                        wrapper.setPacketType(ClientboundPackets1_18.SPAWN_MOB);
                    }
                    else if (entityType == Entity1_19Types.PAINTING) {
                        wrapper.cancel();
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final StoredEntityData entityData = EntityPackets1_19.this.tracker(wrapper.user()).entityData(entityId);
                        final Position position = new Position(wrapper.get((Type<Double>)Type.DOUBLE, 0).intValue(), wrapper.get((Type<Double>)Type.DOUBLE, 1).intValue(), wrapper.get((Type<Double>)Type.DOUBLE, 2).intValue());
                        entityData.put(new StoredPainting(entityId, wrapper.get(Type.UUID, 0), position, data));
                    }
                    else {
                        if (entityType == Entity1_19Types.FALLING_BLOCK) {
                            data = ((Protocol1_18_2To1_19)EntityPackets1_19.this.protocol).getMappingData().getNewBlockStateId(data);
                        }
                        wrapper.write(Type.INT, data);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_19.ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler(wrapper -> {
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.read(Type.NBT);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_19.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    final DimensionRegistryStorage dimensionRegistryStorage = wrapper.user().get(DimensionRegistryStorage.class);
                    dimensionRegistryStorage.clear();
                    final String dimensionKey = wrapper.read(Type.STRING);
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final ListTag dimensions = registry.get("minecraft:dimension_type").get("value");
                    boolean found = false;
                    dimensions.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag dimension = iterator.next();
                        final CompoundTag dimensionCompound = (CompoundTag)dimension;
                        final StringTag nameTag = dimensionCompound.get("name");
                        final CompoundTag dimensionData = dimensionCompound.get("element");
                        dimensionRegistryStorage.addDimension(nameTag.getValue(), dimensionData.clone());
                        if (!found && nameTag.getValue().equals(dimensionKey)) {
                            wrapper.write(Type.NBT, dimensionData);
                            found = true;
                        }
                    }
                    if (!found) {
                        new IllegalStateException("Could not find dimension " + dimensionKey + " in dimension registry");
                        throw;
                    }
                    else {
                        final CompoundTag biomeRegistry = registry.get("minecraft:worldgen/biome");
                        final ListTag biomes = biomeRegistry.get("value");
                        biomes.getValue().iterator();
                        final Iterator iterator2;
                        while (iterator2.hasNext()) {
                            final Tag biome = iterator2.next();
                            final CompoundTag biomeCompound = ((CompoundTag)biome).get("element");
                            biomeCompound.put("category", new StringTag("none"));
                        }
                        EntityPackets1_19.this.tracker(wrapper.user()).setBiomesSent(biomes.size());
                        final ListTag chatTypes = registry.remove("minecraft:chat_type").get("value");
                        chatTypes.iterator();
                        final Iterator iterator3;
                        while (iterator3.hasNext()) {
                            final Tag chatType = iterator3.next();
                            final CompoundTag chatTypeCompound = (CompoundTag)chatType;
                            final NumberTag idTag = chatTypeCompound.get("id");
                            dimensionRegistryStorage.addChatType(idTag.asInt(), chatTypeCompound);
                        }
                        return;
                    }
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.read(Type.OPTIONAL_GLOBAL_POSITION);
                this.handler(EntityPackets1_19.this.worldDataTrackerHandler(1));
                this.handler(EntityPackets1_19.this.playerTrackerHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_19.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final String dimensionKey = wrapper.read(Type.STRING);
                    final CompoundTag dimension = wrapper.user().get(DimensionRegistryStorage.class).dimension(dimensionKey);
                    if (dimension == null) {
                        new IllegalArgumentException("Could not find dimension " + dimensionKey + " in dimension registry");
                        throw;
                    }
                    else {
                        wrapper.write(Type.NBT, dimension);
                        return;
                    }
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.read(Type.OPTIONAL_GLOBAL_POSITION);
                this.handler(EntityPackets1_19.this.worldDataTrackerHandler(0));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_19.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    for (int entries = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < entries; ++i) {
                        wrapper.passthrough(Type.UUID);
                        if (action == 0) {
                            wrapper.passthrough(Type.STRING);
                            for (int properties = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < properties; ++j) {
                                wrapper.passthrough(Type.STRING);
                                wrapper.passthrough(Type.STRING);
                                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                    wrapper.passthrough(Type.STRING);
                                }
                            }
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.COMPONENT);
                            }
                            wrapper.read(Type.OPTIONAL_PROFILE_KEY);
                        }
                        else if (action == 1 || action == 2) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        else if (action == 3 && wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.COMPONENT);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public void onMappingDataLoaded() {
        this.filter().handler((event, meta) -> {
            if (meta.metaType().typeId() <= Types1_18.META_TYPES.poseType.typeId()) {
                meta.setMetaType(Types1_18.META_TYPES.byId(meta.metaType().typeId()));
            }
            final MetaType type = meta.metaType();
            if (type == Types1_18.META_TYPES.particleType) {
                final Particle particle = (Particle)meta.getValue();
                final ParticleMappings particleMappings = ((Protocol1_18_2To1_19)this.protocol).getMappingData().getParticleMappings();
                if (particle.getId() == particleMappings.id("sculk_charge")) {
                    event.cancel();
                }
                else if (particle.getId() == particleMappings.id("shriek")) {
                    event.cancel();
                }
                else if (particle.getId() == particleMappings.id("vibration")) {
                    event.cancel();
                }
                else {
                    this.rewriteParticle(particle);
                }
            }
            else if (type == Types1_18.META_TYPES.poseType) {
                final int pose = meta.value();
                if (pose >= 8) {
                    meta.setValue(0);
                }
            }
            return;
        });
        this.registerMetaTypeHandler(Types1_18.META_TYPES.itemType, Types1_18.META_TYPES.blockStateType, null, Types1_18.META_TYPES.optionalComponentType);
        this.mapTypes();
        this.filter().filterFamily(Entity1_19Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int data = (int)meta.getValue();
            meta.setValue(((Protocol1_18_2To1_19)this.protocol).getMappingData().getNewBlockStateId(data));
            return;
        });
        this.filter().type(Entity1_19Types.PAINTING).index(8).handler((event, meta) -> {
            event.cancel();
            final StoredEntityData entityData = this.tracker(event.user()).entityDataIfPresent(event.entityId());
            final StoredPainting storedPainting = entityData.remove(StoredPainting.class);
            if (storedPainting != null) {
                final PacketWrapper packet = PacketWrapper.create(ClientboundPackets1_18.SPAWN_PAINTING, event.user());
                packet.write(Type.VAR_INT, storedPainting.entityId());
                packet.write(Type.UUID, storedPainting.uuid());
                packet.write((Type<Object>)Type.VAR_INT, meta.value());
                packet.write(Type.POSITION1_14, storedPainting.position());
                packet.write(Type.BYTE, storedPainting.direction());
                try {
                    packet.send(Protocol1_18_2To1_19.class);
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return;
        });
        this.filter().type(Entity1_19Types.CAT).index(19).handler((event, meta) -> meta.setMetaType(Types1_18.META_TYPES.varIntType));
        this.filter().type(Entity1_19Types.FROG).cancel(16);
        this.filter().type(Entity1_19Types.FROG).cancel(17);
        this.filter().type(Entity1_19Types.FROG).cancel(18);
        this.mapEntityTypeWithData(Entity1_19Types.FROG, Entity1_19Types.RABBIT).jsonName();
        this.mapEntityTypeWithData(Entity1_19Types.TADPOLE, Entity1_19Types.PUFFERFISH).jsonName();
        this.mapEntityTypeWithData(Entity1_19Types.CHEST_BOAT, Entity1_19Types.BOAT);
        this.filter().type(Entity1_19Types.WARDEN).cancel(16);
        this.mapEntityTypeWithData(Entity1_19Types.WARDEN, Entity1_19Types.IRON_GOLEM).jsonName();
        this.mapEntityTypeWithData(Entity1_19Types.ALLAY, Entity1_19Types.VEX).jsonName();
        this.filter().type(Entity1_19Types.GOAT).cancel(18);
        this.filter().type(Entity1_19Types.GOAT).cancel(19);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_19Types.getTypeFromId(typeId);
    }
}
