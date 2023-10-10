// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.List;
import java.util.function.Function;
import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.ParrotStorage;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_12;
import com.viaversion.viaversion.api.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import com.viaversion.viaversion.api.type.types.version.Types1_12;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.ObjectType;
import com.viaversion.viabackwards.utils.Block;
import java.util.Optional;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_12Types;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_12 extends LegacyEntityRewriter<Protocol1_11_1To1_12>
{
    public EntityPackets1_12(final Protocol1_11_1To1_12 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.SPAWN_ENTITY, new PacketRemapper() {
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
                this.handler(LegacyEntityRewriter.this.getObjectRewriter(id -> Entity1_12Types.ObjectType.findById(id).orElse(null)));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Optional<Entity1_12Types.ObjectType> type = Entity1_12Types.ObjectType.findById(wrapper.get((Type<Byte>)Type.BYTE, 0));
                        if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                            final int objectData = wrapper.get((Type<Integer>)Type.INT, 0);
                            final int objType = objectData & 0xFFF;
                            final int data = objectData >> 12 & 0xF;
                            final Block block = ((Protocol1_11_1To1_12)EntityPackets1_12.this.protocol).getItemRewriter().handleBlock(objType, data);
                            if (block == null) {
                                return;
                            }
                            wrapper.set(Type.INT, 0, block.getId() | block.getData() << 12);
                        }
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_12.SPAWN_EXPERIENCE_ORB, Entity1_12Types.EntityType.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_12.SPAWN_GLOBAL_ENTITY, Entity1_12Types.EntityType.WEATHER);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.SPAWN_MOB, new PacketRemapper() {
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
                this.map(Types1_12.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter(Types1_12.METADATA_LIST));
            }
        });
        this.registerTracker(ClientboundPackets1_12.SPAWN_PAINTING, Entity1_12Types.EntityType.PAINTING);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_12.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_12.METADATA_LIST, Entity1_12Types.EntityType.PLAYER));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler(Entity1_12Types.EntityType.PLAYER, Type.INT));
                this.handler(EntityRewriterBase.this.getDimensionHandler(1));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ShoulderTracker tracker = wrapper.user().get(ShoulderTracker.class);
                        tracker.setEntityId(wrapper.get((Type<Integer>)Type.INT, 0));
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PacketWrapper wrapper = PacketWrapper.create(7, null, packetWrapper.user());
                        wrapper.write(Type.VAR_INT, 1);
                        wrapper.write(Type.STRING, "achievement.openInventory");
                        wrapper.write(Type.VAR_INT, 1);
                        wrapper.scheduleSend(Protocol1_11_1To1_12.class);
                    }
                });
            }
        });
        this.registerRespawn(ClientboundPackets1_12.RESPAWN);
        this.registerRemoveEntities(ClientboundPackets1_12.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_12.ENTITY_METADATA, Types1_12.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_12.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                int newSize;
                int size;
                int i;
                String key;
                int modSize;
                int j;
                int modSize2;
                int k;
                this.handler(wrapper -> {
                    for (size = (newSize = wrapper.get((Type<Integer>)Type.INT, 0)), i = 0; i < size; ++i) {
                        key = wrapper.read(Type.STRING);
                        if (key.equals("generic.flyingSpeed")) {
                            --newSize;
                            wrapper.read((Type<Object>)Type.DOUBLE);
                            for (modSize = wrapper.read((Type<Integer>)Type.VAR_INT), j = 0; j < modSize; ++j) {
                                wrapper.read(Type.UUID);
                                wrapper.read((Type<Object>)Type.DOUBLE);
                                wrapper.read((Type<Object>)Type.BYTE);
                            }
                        }
                        else {
                            wrapper.write(Type.STRING, key);
                            wrapper.passthrough((Type<Object>)Type.DOUBLE);
                            for (modSize2 = wrapper.passthrough((Type<Integer>)Type.VAR_INT), k = 0; k < modSize2; ++k) {
                                wrapper.passthrough(Type.UUID);
                                wrapper.passthrough((Type<Object>)Type.DOUBLE);
                                wrapper.passthrough((Type<Object>)Type.BYTE);
                            }
                        }
                    }
                    if (newSize != size) {
                        wrapper.set(Type.INT, 0, newSize);
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.mapEntityTypeWithData(Entity1_12Types.EntityType.PARROT, Entity1_12Types.EntityType.BAT).plainName().spawnMetadata(storage -> storage.add(new Metadata(12, MetaType1_12.Byte, 0)));
        this.mapEntityTypeWithData(Entity1_12Types.EntityType.ILLUSION_ILLAGER, Entity1_12Types.EntityType.EVOCATION_ILLAGER).plainName();
        this.filter().filterFamily(Entity1_12Types.EntityType.EVOCATION_ILLAGER).cancel(12);
        this.filter().filterFamily(Entity1_12Types.EntityType.EVOCATION_ILLAGER).index(13).toIndex(12);
        byte mask;
        this.filter().type(Entity1_12Types.EntityType.ILLUSION_ILLAGER).index(0).handler((event, meta) -> {
            mask = (byte)meta.getValue();
            if ((mask & 0x20) == 0x20) {
                mask &= 0xFFFFFFDF;
            }
            meta.setValue(mask);
            return;
        });
        final StoredEntityData data;
        this.filter().filterFamily(Entity1_12Types.EntityType.PARROT).handler((event, meta) -> {
            data = this.storedEntityData(event);
            if (!data.has(ParrotStorage.class)) {
                data.put(new ParrotStorage());
            }
            return;
        });
        this.filter().type(Entity1_12Types.EntityType.PARROT).cancel(12);
        final StoredEntityData data2;
        final ParrotStorage storage2;
        final boolean isSitting;
        final boolean isTamed;
        this.filter().type(Entity1_12Types.EntityType.PARROT).index(13).handler((event, meta) -> {
            data2 = this.storedEntityData(event);
            storage2 = data2.get(ParrotStorage.class);
            isSitting = (((byte)meta.getValue() & 0x1) == 0x1);
            isTamed = (((byte)meta.getValue() & 0x4) == 0x4);
            if (storage2.isTamed() || isTamed) {}
            storage2.setTamed(isTamed);
            if (isSitting) {
                event.setIndex(12);
                meta.setValue(1);
                storage2.setSitting(true);
            }
            else if (storage2.isSitting()) {
                event.setIndex(12);
                meta.setValue(0);
                storage2.setSitting(false);
            }
            else {
                event.cancel();
            }
            return;
        });
        this.filter().type(Entity1_12Types.EntityType.PARROT).cancel(14);
        this.filter().type(Entity1_12Types.EntityType.PARROT).cancel(15);
        final CompoundTag tag;
        final ShoulderTracker tracker;
        String id;
        this.filter().type(Entity1_12Types.EntityType.PLAYER).index(15).handler((event, meta) -> {
            tag = (CompoundTag)meta.getValue();
            tracker = event.user().get(ShoulderTracker.class);
            if (tag.isEmpty() && tracker.getLeftShoulder() != null) {
                tracker.setLeftShoulder(null);
                tracker.update();
            }
            else if (tag.contains("id") && event.entityId() == tracker.getEntityId()) {
                id = (String)tag.get("id").getValue();
                if (tracker.getLeftShoulder() == null || !tracker.getLeftShoulder().equals(id)) {
                    tracker.setLeftShoulder(id);
                    tracker.update();
                }
            }
            event.cancel();
            return;
        });
        final CompoundTag tag2;
        final ShoulderTracker tracker2;
        String id2;
        this.filter().type(Entity1_12Types.EntityType.PLAYER).index(16).handler((event, meta) -> {
            tag2 = (CompoundTag)event.meta().getValue();
            tracker2 = event.user().get(ShoulderTracker.class);
            if (tag2.isEmpty() && tracker2.getRightShoulder() != null) {
                tracker2.setRightShoulder(null);
                tracker2.update();
            }
            else if (tag2.contains("id") && event.entityId() == tracker2.getEntityId()) {
                id2 = (String)tag2.get("id").getValue();
                if (tracker2.getRightShoulder() == null || !tracker2.getRightShoulder().equals(id2)) {
                    tracker2.setRightShoulder(id2);
                    tracker2.update();
                }
            }
            event.cancel();
        });
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_12Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return Entity1_12Types.getTypeFromId(typeId, true);
    }
}
