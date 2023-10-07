// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.List;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.rewriter.meta.MetaHandler;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.VillagerData;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.DifficultyStorage;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import java.util.function.Supplier;
import com.viaversion.viabackwards.api.entities.storage.EntityPositionStorage;
import com.viaversion.viabackwards.api.rewriters.EntityRewriterBase;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viabackwards.api.entities.storage.EntityPositionHandler;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_14 extends LegacyEntityRewriter<Protocol1_13_2To1_14>
{
    private EntityPositionHandler positionHandler;
    
    public EntityPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol, Types1_13_2.META_TYPES.optionalComponentType, Types1_13_2.META_TYPES.booleanType);
    }
    
    @Override
    protected void addTrackedEntity(final PacketWrapper wrapper, final int entityId, final EntityType type) throws Exception {
        super.addTrackedEntity(wrapper, entityId, type);
        if (type == Entity1_14Types.PAINTING) {
            final Position position = wrapper.get(Type.POSITION, 0);
            this.positionHandler.cacheEntityPosition(wrapper, position.getX(), position.getY(), position.getZ(), true, false);
        }
        else if (wrapper.getId() != ClientboundPackets1_14.JOIN_GAME.getId()) {
            this.positionHandler.cacheEntityPosition(wrapper, true, false);
        }
    }
    
    @Override
    protected void registerPackets() {
        this.positionHandler = new EntityPositionHandler(this, EntityPositionStorage1_14.class, EntityPositionStorage1_14::new);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int entityId = wrapper.passthrough((Type<Integer>)Type.INT);
                    final byte status = wrapper.passthrough((Type<Byte>)Type.BYTE);
                    if (status == 3) {
                        final EntityTracker tracker = EntityPackets1_14.this.tracker(wrapper.user());
                        final EntityType entityType = tracker.entityType(entityId);
                        if (entityType == Entity1_14Types.PLAYER) {
                            for (int i = 0; i <= 5; ++i) {
                                final PacketWrapper equipmentPacket = wrapper.create(ClientboundPackets1_13.ENTITY_EQUIPMENT);
                                equipmentPacket.write(Type.VAR_INT, entityId);
                                equipmentPacket.write(Type.VAR_INT, i);
                                equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, null);
                                equipmentPacket.send(Protocol1_13_2To1_14.class);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_TELEPORT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, false, false));
            }
        });
        final PacketRemapper relativeMoveHandler = new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final double x = wrapper.get((Type<Short>)Type.SHORT, 0) / 4096.0;
                        final double y = wrapper.get((Type<Short>)Type.SHORT, 1) / 4096.0;
                        final double z = wrapper.get((Type<Short>)Type.SHORT, 2) / 4096.0;
                        EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, x, y, z, false, true);
                    }
                });
            }
        };
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_POSITION, relativeMoveHandler);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_POSITION_AND_ROTATION, relativeMoveHandler);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int mappedId = EntityPackets1_14.this.newEntityId(id);
                        final Entity1_13Types.EntityType entityType = Entity1_13Types.getTypeFromId(mappedId, false);
                        Entity1_13Types.ObjectType objectType;
                        if (entityType.isOrHasParent(Entity1_13Types.EntityType.MINECART_ABSTRACT)) {
                            objectType = Entity1_13Types.ObjectType.MINECART;
                            int data = 0;
                            switch (entityType) {
                                case CHEST_MINECART: {
                                    data = 1;
                                    break;
                                }
                                case FURNACE_MINECART: {
                                    data = 2;
                                    break;
                                }
                                case TNT_MINECART: {
                                    data = 3;
                                    break;
                                }
                                case SPAWNER_MINECART: {
                                    data = 4;
                                    break;
                                }
                                case HOPPER_MINECART: {
                                    data = 5;
                                    break;
                                }
                                case COMMAND_BLOCK_MINECART: {
                                    data = 6;
                                    break;
                                }
                            }
                            if (data != 0) {
                                wrapper.set(Type.INT, 0, data);
                            }
                        }
                        else {
                            objectType = Entity1_13Types.ObjectType.fromEntityType(entityType).orElse(null);
                        }
                        if (objectType == null) {
                            return;
                        }
                        wrapper.set(Type.BYTE, 0, (byte)objectType.getId());
                        int data = wrapper.get((Type<Integer>)Type.INT, 0);
                        if (objectType == Entity1_13Types.ObjectType.FALLING_BLOCK) {
                            final int blockState = wrapper.get((Type<Integer>)Type.INT, 0);
                            final int combined = ((Protocol1_13_2To1_14)EntityPackets1_14.this.protocol).getMappingData().getNewBlockStateId(blockState);
                            wrapper.set(Type.INT, 0, combined);
                        }
                        else if (entityType.isOrHasParent(Entity1_13Types.EntityType.ABSTRACT_ARROW)) {
                            wrapper.set(Type.INT, 0, data + 1);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_MOB, new PacketRemapper() {
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
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                        final EntityType entityType = Entity1_14Types.getTypeFromId(type);
                        EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), entityType);
                        final int oldId = EntityPackets1_14.this.newEntityId(type);
                        if (oldId == -1) {
                            final EntityData entityData = EntityRewriterBase.this.entityDataForType(entityType);
                            if (entityData == null) {
                                ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13.2 entity type for 1.14 entity type " + type + "/" + entityType);
                                wrapper.cancel();
                            }
                            else {
                                wrapper.set(Type.VAR_INT, 1, entityData.replacementId());
                            }
                        }
                        else {
                            wrapper.set(Type.VAR_INT, 1, oldId);
                        }
                    }
                });
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter(Types1_13_2.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), Entity1_14Types.EXPERIENCE_ORB));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), Entity1_14Types.LIGHTNING_BOLT));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.BYTE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), Entity1_14Types.PAINTING));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_13_2.METADATA_LIST, Entity1_14Types.PLAYER));
                this.handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, true, false));
            }
        });
        this.registerRemoveEntities(ClientboundPackets1_14.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler(Entity1_14Types.PLAYER, Type.INT));
                this.handler(EntityRewriterBase.this.getDimensionHandler(1));
                this.handler(wrapper -> {
                    final short difficulty = wrapper.user().get(DifficultyStorage.class).getDifficulty();
                    wrapper.write(Type.UNSIGNED_BYTE, difficulty);
                    wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                    wrapper.passthrough(Type.STRING);
                    wrapper.read((Type<Object>)Type.VAR_INT);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 0);
                    clientWorld.setEnvironment(dimensionId);
                    final short difficulty = wrapper.user().get(DifficultyStorage.class).getDifficulty();
                    wrapper.write(Type.UNSIGNED_BYTE, difficulty);
                    wrapper.user().get(ChunkLightStorage.class).clear();
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.mapTypes(Entity1_14Types.values(), Entity1_13Types.EntityType.class);
        this.mapEntityTypeWithData(Entity1_14Types.CAT, Entity1_14Types.OCELOT).jsonName();
        this.mapEntityTypeWithData(Entity1_14Types.TRADER_LLAMA, Entity1_14Types.LLAMA).jsonName();
        this.mapEntityTypeWithData(Entity1_14Types.FOX, Entity1_14Types.WOLF).jsonName();
        this.mapEntityTypeWithData(Entity1_14Types.PANDA, Entity1_14Types.POLAR_BEAR).jsonName();
        this.mapEntityTypeWithData(Entity1_14Types.PILLAGER, Entity1_14Types.VILLAGER).jsonName();
        this.mapEntityTypeWithData(Entity1_14Types.WANDERING_TRADER, Entity1_14Types.VILLAGER).jsonName();
        this.mapEntityTypeWithData(Entity1_14Types.RAVAGER, Entity1_14Types.COW).jsonName();
        this.filter().handler((event, meta) -> {
            final int typeId = meta.metaType().typeId();
            if (typeId <= 15) {
                meta.setMetaType(Types1_13_2.META_TYPES.byId(typeId));
            }
            final MetaType type = meta.metaType();
            if (type == Types1_13_2.META_TYPES.itemType) {
                final Item item = (Item)meta.getValue();
                meta.setValue(((Protocol1_13_2To1_14)this.protocol).getItemRewriter().handleItemToClient(item));
            }
            else if (type == Types1_13_2.META_TYPES.blockStateType) {
                final int blockstate = meta.value();
                meta.setValue(((Protocol1_13_2To1_14)this.protocol).getMappingData().getNewBlockStateId(blockstate));
            }
            return;
        });
        this.filter().type(Entity1_14Types.PILLAGER).cancel(15);
        this.filter().type(Entity1_14Types.FOX).cancel(15);
        this.filter().type(Entity1_14Types.FOX).cancel(16);
        this.filter().type(Entity1_14Types.FOX).cancel(17);
        this.filter().type(Entity1_14Types.FOX).cancel(18);
        this.filter().type(Entity1_14Types.PANDA).cancel(15);
        this.filter().type(Entity1_14Types.PANDA).cancel(16);
        this.filter().type(Entity1_14Types.PANDA).cancel(17);
        this.filter().type(Entity1_14Types.PANDA).cancel(18);
        this.filter().type(Entity1_14Types.PANDA).cancel(19);
        this.filter().type(Entity1_14Types.PANDA).cancel(20);
        this.filter().type(Entity1_14Types.CAT).cancel(18);
        this.filter().type(Entity1_14Types.CAT).cancel(19);
        this.filter().type(Entity1_14Types.CAT).cancel(20);
        this.filter().handler((event, meta) -> {
            final EntityType type2 = event.entityType();
            if (type2 == null) {
                return;
            }
            else {
                if (type2.isOrHasParent(Entity1_14Types.ABSTRACT_ILLAGER_BASE) || type2 == Entity1_14Types.RAVAGER || type2 == Entity1_14Types.WITCH) {
                    final int index = event.index();
                    if (index == 14) {
                        event.cancel();
                    }
                    else if (index > 14) {
                        event.setIndex(index - 1);
                    }
                }
                return;
            }
        });
        this.filter().type(Entity1_14Types.AREA_EFFECT_CLOUD).index(10).handler((event, meta) -> this.rewriteParticle((Particle)meta.getValue()));
        this.filter().type(Entity1_14Types.FIREWORK_ROCKET).index(8).handler((event, meta) -> {
            meta.setMetaType(Types1_13_2.META_TYPES.varIntType);
            final Integer value = (Integer)meta.getValue();
            if (value == null) {
                meta.setValue(0);
            }
            return;
        });
        this.filter().filterFamily(Entity1_14Types.ABSTRACT_ARROW).removeIndex(9);
        this.filter().type(Entity1_14Types.VILLAGER).cancel(15);
        final MetaHandler villagerDataHandler = (event, meta) -> {
            final VillagerData villagerData = (VillagerData)meta.getValue();
            meta.setTypeAndValue(Types1_13_2.META_TYPES.varIntType, this.villagerDataToProfession(villagerData));
            if (meta.id() == 16) {
                event.setIndex(15);
            }
            return;
        };
        this.filter().type(Entity1_14Types.ZOMBIE_VILLAGER).index(18).handler(villagerDataHandler);
        this.filter().type(Entity1_14Types.VILLAGER).index(16).handler(villagerDataHandler);
        this.filter().filterFamily(Entity1_14Types.ABSTRACT_SKELETON).index(13).handler((event, meta) -> {
            final byte value2 = (byte)meta.getValue();
            if ((value2 & 0x4) != 0x0) {
                event.createExtraMeta(new Metadata(14, Types1_13_2.META_TYPES.booleanType, true));
            }
            return;
        });
        this.filter().filterFamily(Entity1_14Types.ZOMBIE).index(13).handler((event, meta) -> {
            final byte value3 = (byte)meta.getValue();
            if ((value3 & 0x4) != 0x0) {
                event.createExtraMeta(new Metadata(16, Types1_13_2.META_TYPES.booleanType, true));
            }
            return;
        });
        this.filter().filterFamily(Entity1_14Types.ZOMBIE).addIndex(16);
        this.filter().filterFamily(Entity1_14Types.LIVINGENTITY).handler((event, meta) -> {
            final int index2 = event.index();
            if (index2 == 12) {
                final Position position = (Position)meta.getValue();
                if (position != null) {
                    final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_13.USE_BED, null, event.user());
                    wrapper.write(Type.VAR_INT, event.entityId());
                    wrapper.write(Type.POSITION, position);
                    try {
                        wrapper.scheduleSend(Protocol1_13_2To1_14.class);
                    }
                    catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                }
                event.cancel();
            }
            else if (index2 > 12) {
                event.setIndex(index2 - 1);
            }
            return;
        });
        this.filter().removeIndex(6);
        this.filter().type(Entity1_14Types.OCELOT).index(13).handler((event, meta) -> {
            event.setIndex(15);
            meta.setTypeAndValue(Types1_13_2.META_TYPES.varIntType, 0);
            return;
        });
        this.filter().type(Entity1_14Types.CAT).handler((event, meta) -> {
            if (event.index() == 15) {
                meta.setValue(1);
            }
            else if (event.index() == 13) {
                meta.setValue((byte)((byte)meta.getValue() & 0x4));
            }
            return;
        });
        this.filter().handler((event, meta) -> {
            if (meta.metaType().typeId() > 15) {
                new IllegalArgumentException("Unhandled metadata: " + meta);
                throw;
            }
        });
    }
    
    public int villagerDataToProfession(final VillagerData data) {
        switch (data.getProfession()) {
            case 1:
            case 10:
            case 13:
            case 14: {
                return 3;
            }
            case 2:
            case 8: {
                return 4;
            }
            case 3:
            case 9: {
                return 1;
            }
            case 4: {
                return 2;
            }
            case 5:
            case 6:
            case 7:
            case 12: {
                return 0;
            }
            default: {
                return 5;
            }
        }
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_14Types.getTypeFromId(typeId);
    }
}
