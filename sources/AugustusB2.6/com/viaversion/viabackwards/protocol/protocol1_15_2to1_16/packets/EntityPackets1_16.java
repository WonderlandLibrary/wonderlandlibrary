// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.storage.WolfDataMaskStorage;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_15Types;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.WorldNameTracker;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public class EntityPackets1_16 extends EntityRewriter<Protocol1_15_2To1_16>
{
    private final ValueTransformer<String, Integer> dimensionTransformer;
    
    public EntityPackets1_16(final Protocol1_15_2To1_16 protocol) {
        super(protocol);
        this.dimensionTransformer = new ValueTransformer<String, Integer>((Type)Type.STRING, (Type)Type.INT) {
            @Override
            public Integer transform(final PacketWrapper wrapper, final String input) {
                switch (input) {
                    case "minecraft:the_nether": {
                        return -1;
                    }
                    default: {
                        return 0;
                    }
                    case "minecraft:the_end": {
                        return 1;
                    }
                }
            }
        };
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.SPAWN_ENTITY, new PacketRemapper() {
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
                this.map(Type.INT);
                final EntityType entityType;
                PacketWrapper spawnLightningPacket;
                this.handler(wrapper -> {
                    entityType = EntityPackets1_16.this.typeFromId(wrapper.get((Type<Integer>)Type.VAR_INT, 1));
                    if (entityType == Entity1_16Types.LIGHTNING_BOLT) {
                        wrapper.cancel();
                        spawnLightningPacket = wrapper.create(ClientboundPackets1_15.SPAWN_GLOBAL_ENTITY);
                        spawnLightningPacket.write(Type.VAR_INT, (Integer)wrapper.get((Type<T>)Type.VAR_INT, 0));
                        spawnLightningPacket.write(Type.BYTE, (Byte)1);
                        spawnLightningPacket.write(Type.DOUBLE, (Double)wrapper.get((Type<T>)Type.DOUBLE, 0));
                        spawnLightningPacket.write(Type.DOUBLE, (Double)wrapper.get((Type<T>)Type.DOUBLE, 1));
                        spawnLightningPacket.write(Type.DOUBLE, (Double)wrapper.get((Type<T>)Type.DOUBLE, 2));
                        spawnLightningPacket.send(Protocol1_15_2To1_16.class);
                    }
                    return;
                });
                this.handler(EntityPackets1_16.this.getSpawnTrackerWithDataHandler(Entity1_16Types.FALLING_BLOCK));
            }
        });
        this.registerSpawnTracker(ClientboundPackets1_16.SPAWN_MOB);
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map((ValueTransformer<Object, Object>)EntityPackets1_16.this.dimensionTransformer);
                final WorldNameTracker worldNameTracker;
                final String nextWorldName;
                final ClientWorld clientWorld;
                final int dimension;
                PacketWrapper packet;
                this.handler(wrapper -> {
                    worldNameTracker = wrapper.user().get(WorldNameTracker.class);
                    nextWorldName = wrapper.read(Type.STRING);
                    wrapper.passthrough((Type<Object>)Type.LONG);
                    wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                    wrapper.read((Type<Object>)Type.BYTE);
                    clientWorld = wrapper.user().get(ClientWorld.class);
                    dimension = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (clientWorld.getEnvironment() != null && dimension == clientWorld.getEnvironment().getId() && (wrapper.user().isClientSide() || Via.getPlatform().isProxy() || wrapper.user().getProtocolInfo().getProtocolVersion() <= ProtocolVersion.v1_12_2.getVersion() || !nextWorldName.equals(worldNameTracker.getWorldName()))) {
                        packet = wrapper.create(ClientboundPackets1_15.RESPAWN);
                        packet.write(Type.INT, (dimension == 0) ? -1 : 0);
                        packet.write(Type.LONG, 0L);
                        packet.write(Type.UNSIGNED_BYTE, (Short)0);
                        packet.write(Type.STRING, "default");
                        packet.send(Protocol1_15_2To1_16.class);
                    }
                    clientWorld.setEnvironment(dimension);
                    wrapper.write(Type.STRING, "default");
                    wrapper.read((Type<Object>)Type.BOOLEAN);
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.set(Type.STRING, 0, "flat");
                    }
                    wrapper.read((Type<Object>)Type.BOOLEAN);
                    worldNameTracker.setWorldName(nextWorldName);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.NOTHING);
                this.map(Type.STRING_ARRAY, Type.NOTHING);
                this.map(Type.NBT, Type.NOTHING);
                this.map((ValueTransformer<Object, Object>)EntityPackets1_16.this.dimensionTransformer);
                final WorldNameTracker worldNameTracker;
                this.handler(wrapper -> {
                    worldNameTracker = wrapper.user().get(WorldNameTracker.class);
                    worldNameTracker.setWorldName(wrapper.read(Type.STRING));
                    return;
                });
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                final ClientWorld clientChunks;
                this.handler(wrapper -> {
                    clientChunks = wrapper.user().get(ClientWorld.class);
                    clientChunks.setEnvironment(wrapper.get((Type<Integer>)Type.INT, 1));
                    ((com.viaversion.viaversion.api.rewriter.EntityRewriter<Protocol>)EntityPackets1_16.this).tracker(wrapper.user()).addEntity(wrapper.get((Type<Integer>)Type.INT, 0), Entity1_16Types.PLAYER);
                    wrapper.write(Type.STRING, "default");
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.read((Type<Object>)Type.BOOLEAN);
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.set(Type.STRING, 0, "flat");
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_16.SPAWN_EXPERIENCE_ORB, Entity1_16Types.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_16.SPAWN_PAINTING, Entity1_16Types.PAINTING);
        this.registerTracker(ClientboundPackets1_16.SPAWN_PLAYER, Entity1_16Types.PLAYER);
        this.registerRemoveEntities(ClientboundPackets1_16.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_16.ENTITY_METADATA, Types1_16.METADATA_LIST, Types1_14.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                int size;
                int i;
                String attributeIdentifier;
                String oldKey;
                int modifierSize;
                int j;
                this.handler(wrapper -> {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    for (size = wrapper.passthrough((Type<Integer>)Type.INT), i = 0; i < size; ++i) {
                        attributeIdentifier = wrapper.read(Type.STRING);
                        oldKey = ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getMappingData().getAttributeMappings().get(attributeIdentifier);
                        wrapper.write(Type.STRING, (oldKey != null) ? oldKey : attributeIdentifier.replace("minecraft:", ""));
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        for (modifierSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < modifierSize; ++j) {
                            wrapper.passthrough(Type.UUID);
                            wrapper.passthrough((Type<Object>)Type.DOUBLE);
                            wrapper.passthrough((Type<Object>)Type.BYTE);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int action;
                int playerCount;
                int i;
                int properties;
                int j;
                this.handler(packetWrapper -> {
                    action = packetWrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    for (playerCount = packetWrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < playerCount; ++i) {
                        packetWrapper.passthrough(Type.UUID);
                        if (action == 0) {
                            packetWrapper.passthrough(Type.STRING);
                            for (properties = packetWrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < properties; ++j) {
                                packetWrapper.passthrough(Type.STRING);
                                packetWrapper.passthrough(Type.STRING);
                                if (packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                    packetWrapper.passthrough(Type.STRING);
                                }
                            }
                            packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                            packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                            if (packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getTranslatableRewriter().processText(packetWrapper.passthrough(Type.COMPONENT));
                            }
                        }
                        else if (action == 1) {
                            packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        else if (action == 2) {
                            packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        else if (action == 3 && packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getTranslatableRewriter().processText(packetWrapper.passthrough(Type.COMPONENT));
                        }
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        final MetaType type;
        JsonElement text;
        this.filter().handler((event, meta) -> {
            meta.setMetaType(Types1_14.META_TYPES.byId(meta.metaType().typeId()));
            type = meta.metaType();
            if (type == Types1_14.META_TYPES.itemType) {
                meta.setValue(((Protocol1_15_2To1_16)this.protocol).getItemRewriter().handleItemToClient((Item)meta.getValue()));
            }
            else if (type == Types1_14.META_TYPES.blockStateType) {
                meta.setValue(((Protocol1_15_2To1_16)this.protocol).getMappingData().getNewBlockStateId((int)meta.getValue()));
            }
            else if (type == Types1_14.META_TYPES.particleType) {
                this.rewriteParticle((Particle)meta.getValue());
            }
            else if (type == Types1_14.META_TYPES.optionalComponentType) {
                text = (JsonElement)meta.value();
                if (text != null) {
                    ((Protocol1_15_2To1_16)this.protocol).getTranslatableRewriter().processText(text);
                }
            }
            return;
        });
        this.mapEntityType(Entity1_16Types.ZOMBIFIED_PIGLIN, Entity1_15Types.ZOMBIE_PIGMAN);
        this.mapTypes(Entity1_16Types.values(), Entity1_15Types.class);
        this.mapEntityTypeWithData(Entity1_16Types.HOGLIN, Entity1_16Types.COW).jsonName();
        this.mapEntityTypeWithData(Entity1_16Types.ZOGLIN, Entity1_16Types.COW).jsonName();
        this.mapEntityTypeWithData(Entity1_16Types.PIGLIN, Entity1_16Types.ZOMBIFIED_PIGLIN).jsonName();
        this.mapEntityTypeWithData(Entity1_16Types.STRIDER, Entity1_16Types.MAGMA_CUBE).jsonName();
        this.filter().type(Entity1_16Types.ZOGLIN).cancel(16);
        this.filter().type(Entity1_16Types.HOGLIN).cancel(15);
        this.filter().type(Entity1_16Types.PIGLIN).cancel(16);
        this.filter().type(Entity1_16Types.PIGLIN).cancel(17);
        this.filter().type(Entity1_16Types.PIGLIN).cancel(18);
        final boolean baby;
        this.filter().type(Entity1_16Types.STRIDER).index(15).handler((event, meta) -> {
            baby = (boolean)meta.value();
            meta.setTypeAndValue(Types1_14.META_TYPES.varIntType, baby ? 1 : 3);
            return;
        });
        this.filter().type(Entity1_16Types.STRIDER).cancel(16);
        this.filter().type(Entity1_16Types.STRIDER).cancel(17);
        this.filter().type(Entity1_16Types.STRIDER).cancel(18);
        this.filter().type(Entity1_16Types.FISHING_BOBBER).cancel(8);
        this.filter().filterFamily(Entity1_16Types.ABSTRACT_ARROW).cancel(8);
        this.filter().filterFamily(Entity1_16Types.ABSTRACT_ARROW).handler((event, meta) -> {
            if (event.index() >= 8) {
                event.setIndex(event.index() + 1);
            }
            return;
        });
        final byte mask;
        final StoredEntityData data;
        this.filter().type(Entity1_16Types.WOLF).index(16).handler((event, meta) -> {
            mask = (byte)meta.value();
            data = ((com.viaversion.viaversion.api.rewriter.EntityRewriter<Protocol>)this).tracker(event.user()).entityData(event.entityId());
            data.put(new WolfDataMaskStorage(mask));
            return;
        });
        final StoredEntityData data2;
        byte previousMask;
        WolfDataMaskStorage wolfData;
        final int angerTime;
        final byte tameableMask;
        this.filter().type(Entity1_16Types.WOLF).index(20).handler((event, meta) -> {
            data2 = ((com.viaversion.viaversion.api.rewriter.EntityRewriter<Protocol>)this).tracker(event.user()).entityDataIfPresent(event.entityId());
            previousMask = 0;
            if (data2 != null) {
                wolfData = data2.get(WolfDataMaskStorage.class);
                if (wolfData != null) {
                    previousMask = wolfData.tameableMask();
                }
            }
            angerTime = (int)meta.value();
            tameableMask = (byte)((angerTime > 0) ? (previousMask | 0x2) : (previousMask & 0xFFFFFFFD));
            event.createExtraMeta(new Metadata(16, Types1_14.META_TYPES.byteType, tameableMask));
            event.cancel();
        });
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_16Types.getTypeFromId(typeId);
    }
}
