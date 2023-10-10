// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_10to1_11.packets;

import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.function.Function;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.ObjectType;
import com.viaversion.viabackwards.utils.Block;
import java.util.Optional;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_12Types;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.PotionSplashHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_11 extends LegacyEntityRewriter<Protocol1_10To1_11>
{
    public EntityPackets1_11(final Protocol1_10To1_11 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                final int type;
                int mappedData;
                this.handler(wrapper -> {
                    type = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (type == 2002 || type == 2007) {
                        if (type == 2007) {
                            wrapper.set(Type.INT, 0, 2002);
                        }
                        mappedData = PotionSplashHandler.getOldData(wrapper.get((Type<Integer>)Type.INT, 1));
                        if (mappedData != -1) {
                            wrapper.set(Type.INT, 1, mappedData);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getObjectRewriter(id -> Entity1_11Types.ObjectType.findById(id).orElse(null)));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Optional<Entity1_12Types.ObjectType> type = Entity1_12Types.ObjectType.findById(wrapper.get((Type<Byte>)Type.BYTE, 0));
                        if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                            final int objectData = wrapper.get((Type<Integer>)Type.INT, 0);
                            final int objType = objectData & 0xFFF;
                            final int data = objectData >> 12 & 0xF;
                            final Block block = ((Protocol1_10To1_11)EntityPackets1_11.this.protocol).getItemRewriter().handleBlock(objType, data);
                            if (block == null) {
                                return;
                            }
                            wrapper.set(Type.INT, 0, block.getId() | block.getData() << 12);
                        }
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, Entity1_11Types.EntityType.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, Entity1_11Types.EntityType.WEATHER);
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_9.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler(Type.UNSIGNED_BYTE, 0));
                final int entityId;
                final EntityType type;
                final List<Metadata> list;
                final EntityData entityData;
                this.handler(wrapper -> {
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    type = ((com.viaversion.viaversion.api.rewriter.EntityRewriter<Protocol>)EntityPackets1_11.this).tracker(wrapper.user()).entityType(entityId);
                    list = wrapper.get(Types1_9.METADATA_LIST, 0);
                    EntityPackets1_11.this.handleMetadata(wrapper.get((Type<Integer>)Type.VAR_INT, 0), list, wrapper.user());
                    entityData = EntityRewriterBase.this.entityDataForType(type);
                    if (entityData != null) {
                        wrapper.set(Type.UNSIGNED_BYTE, 0, (short)entityData.replacementId());
                        if (entityData.hasBaseMeta()) {
                            entityData.defaultMeta().createMeta(new WrappedMetadata(list));
                        }
                    }
                    if (list.isEmpty()) {
                        list.add(new Metadata(0, MetaType1_9.Byte, 0));
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_9_3.SPAWN_PAINTING, Entity1_11Types.EntityType.PAINTING);
        this.registerJoinGame(ClientboundPackets1_9_3.JOIN_GAME, Entity1_11Types.EntityType.PLAYER);
        this.registerRespawn(ClientboundPackets1_9_3.RESPAWN);
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_9.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_9.METADATA_LIST, Entity1_11Types.EntityType.PLAYER));
                final List<Metadata> metadata;
                this.handler(wrapper -> {
                    metadata = wrapper.get(Types1_9.METADATA_LIST, 0);
                    if (metadata.isEmpty()) {
                        metadata.add(new Metadata(0, MetaType1_9.Byte, 0));
                    }
                });
            }
        });
        this.registerRemoveEntities(ClientboundPackets1_9_3.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_9_3.ENTITY_METADATA, Types1_9.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.ENTITY_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte b = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        if (b == 35) {
                            wrapper.clearPacket();
                            wrapper.setId(30);
                            wrapper.write(Type.UNSIGNED_BYTE, (Short)10);
                            wrapper.write(Type.FLOAT, 0.0f);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.ELDER_GUARDIAN, Entity1_11Types.EntityType.GUARDIAN);
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.WITHER_SKELETON, Entity1_11Types.EntityType.SKELETON).plainName().spawnMetadata(storage -> storage.add(this.getSkeletonTypeMeta(1)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.STRAY, Entity1_11Types.EntityType.SKELETON).plainName().spawnMetadata(storage -> storage.add(this.getSkeletonTypeMeta(2)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.HUSK, Entity1_11Types.EntityType.ZOMBIE).plainName().spawnMetadata(storage -> this.handleZombieType(storage, 6));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.ZOMBIE_VILLAGER, Entity1_11Types.EntityType.ZOMBIE).spawnMetadata(storage -> this.handleZombieType(storage, 1));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.HORSE, Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(0)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.DONKEY, Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(1)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.MULE, Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(2)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.SKELETON_HORSE, Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(4)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.ZOMBIE_HORSE, Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(3)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.EVOCATION_FANGS, Entity1_11Types.EntityType.SHULKER);
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.EVOCATION_ILLAGER, Entity1_11Types.EntityType.VILLAGER).plainName();
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.VEX, Entity1_11Types.EntityType.BAT).plainName();
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.VINDICATION_ILLAGER, Entity1_11Types.EntityType.VILLAGER).plainName().spawnMetadata(storage -> storage.add(new Metadata(13, MetaType1_9.VarInt, 4)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.LIAMA, Entity1_11Types.EntityType.HORSE).plainName().spawnMetadata(storage -> storage.add(this.getHorseMetaType(1)));
        this.mapEntityTypeWithData(Entity1_11Types.EntityType.LIAMA_SPIT, Entity1_11Types.EntityType.SNOWBALL);
        this.mapObjectType(Entity1_11Types.ObjectType.LIAMA_SPIT, Entity1_11Types.ObjectType.SNOWBALL, -1);
        this.mapObjectType(Entity1_11Types.ObjectType.EVOCATION_FANGS, Entity1_11Types.ObjectType.FALLING_BLOCK, 4294);
        final boolean b;
        int bitmask;
        this.filter().filterFamily(Entity1_11Types.EntityType.GUARDIAN).index(12).handler((event, meta) -> {
            b = (boolean)meta.getValue();
            bitmask = (b ? 2 : 0);
            if (event.entityType() == Entity1_11Types.EntityType.ELDER_GUARDIAN) {
                bitmask |= 0x4;
            }
            meta.setTypeAndValue(MetaType1_9.Byte, (byte)bitmask);
            return;
        });
        this.filter().filterFamily(Entity1_11Types.EntityType.ABSTRACT_SKELETON).index(12).toIndex(13);
        this.filter().filterFamily(Entity1_11Types.EntityType.ZOMBIE).handler((event, meta) -> {
            switch (meta.id()) {
                case 13: {
                    event.cancel();
                }
                case 14: {
                    event.setIndex(15);
                    break;
                }
                case 15: {
                    event.setIndex(14);
                    break;
                }
                case 16: {
                    event.setIndex(13);
                    meta.setValue(1 + (int)meta.getValue());
                    break;
                }
            }
            return;
        });
        this.filter().type(Entity1_11Types.EntityType.EVOCATION_ILLAGER).index(12).handler((event, meta) -> {
            event.setIndex(13);
            meta.setTypeAndValue(MetaType1_9.VarInt, (int)meta.getValue());
            return;
        });
        this.filter().type(Entity1_11Types.EntityType.VEX).index(12).handler((event, meta) -> meta.setValue(0));
        this.filter().type(Entity1_11Types.EntityType.VINDICATION_ILLAGER).index(12).handler((event, meta) -> {
            event.setIndex(13);
            meta.setTypeAndValue(MetaType1_9.VarInt, (((Number)meta.getValue()).intValue() == 1) ? 2 : 4);
            return;
        });
        final StoredEntityData data;
        final byte b2;
        byte b3;
        this.filter().filterFamily(Entity1_11Types.EntityType.ABSTRACT_HORSE).index(13).handler((event, meta) -> {
            data = this.storedEntityData(event);
            b2 = (byte)meta.getValue();
            if (data.has(ChestedHorseStorage.class) && data.get(ChestedHorseStorage.class).isChested()) {
                b3 = (byte)(b2 | 0x8);
                meta.setValue(b3);
            }
            return;
        });
        final StoredEntityData data2;
        this.filter().filterFamily(Entity1_11Types.EntityType.CHESTED_HORSE).handler((event, meta) -> {
            data2 = this.storedEntityData(event);
            if (!data2.has(ChestedHorseStorage.class)) {
                data2.put(new ChestedHorseStorage());
            }
            return;
        });
        this.filter().type(Entity1_11Types.EntityType.HORSE).index(16).toIndex(17);
        final StoredEntityData data3;
        final ChestedHorseStorage storage2;
        final boolean b4;
        this.filter().filterFamily(Entity1_11Types.EntityType.CHESTED_HORSE).index(15).handler((event, meta) -> {
            data3 = this.storedEntityData(event);
            storage2 = data3.get(ChestedHorseStorage.class);
            b4 = (boolean)meta.getValue();
            storage2.setChested(b4);
            event.cancel();
            return;
        });
        final StoredEntityData data4;
        final ChestedHorseStorage storage3;
        final int index;
        this.filter().type(Entity1_11Types.EntityType.LIAMA).handler((event, meta) -> {
            data4 = this.storedEntityData(event);
            storage3 = data4.get(ChestedHorseStorage.class);
            index = event.index();
            switch (index) {
                case 16: {
                    storage3.setLiamaStrength((int)meta.getValue());
                    event.cancel();
                    break;
                }
                case 17: {
                    storage3.setLiamaCarpetColor((int)meta.getValue());
                    event.cancel();
                    break;
                }
                case 18: {
                    storage3.setLiamaVariant((int)meta.getValue());
                    event.cancel();
                    break;
                }
            }
            return;
        });
        this.filter().filterFamily(Entity1_11Types.EntityType.ABSTRACT_HORSE).index(14).toIndex(16);
        this.filter().type(Entity1_11Types.EntityType.VILLAGER).index(13).handler((event, meta) -> {
            if ((int)meta.getValue() == 5) {
                meta.setValue(0);
            }
            return;
        });
        this.filter().type(Entity1_11Types.EntityType.SHULKER).cancel(15);
    }
    
    private Metadata getSkeletonTypeMeta(final int type) {
        return new Metadata(12, MetaType1_9.VarInt, type);
    }
    
    private Metadata getZombieTypeMeta(final int type) {
        return new Metadata(13, MetaType1_9.VarInt, type);
    }
    
    private void handleZombieType(final WrappedMetadata storage, final int type) {
        final Metadata meta = storage.get(13);
        if (meta == null) {
            storage.add(this.getZombieTypeMeta(type));
        }
    }
    
    private Metadata getHorseMetaType(final int type) {
        return new Metadata(14, MetaType1_9.VarInt, type);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_11Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return Entity1_11Types.getTypeFromId(typeId, true);
    }
}
