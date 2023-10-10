// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_9_4to1_10.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.function.Function;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.entities.ObjectType;
import com.viaversion.viabackwards.utils.Block;
import java.util.Optional;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_12Types;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.protocol.protocol1_9_4to1_10.Protocol1_9_4To1_10;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_10 extends LegacyEntityRewriter<Protocol1_9_4To1_10>
{
    public EntityPackets1_10(final Protocol1_9_4To1_10 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
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
                            final Block block = ((Protocol1_9_4To1_10)EntityPackets1_10.this.protocol).getItemRewriter().handleBlock(objType, data);
                            if (block == null) {
                                return;
                            }
                            wrapper.set(Type.INT, 0, block.getId() | block.getData() << 12);
                        }
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, Entity1_10Types.EntityType.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, Entity1_10Types.EntityType.WEATHER);
        ((AbstractProtocol<ClientboundPackets1_9_3, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.UNSIGNED_BYTE);
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
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityType type = EntityPackets1_10.this.tracker(wrapper.user()).entityType(entityId);
                        final List<Metadata> metadata = wrapper.get(Types1_9.METADATA_LIST, 0);
                        EntityPackets1_10.this.handleMetadata(wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadata, wrapper.user());
                        final EntityData entityData = EntityRewriterBase.this.entityDataForType(type);
                        if (entityData != null) {
                            final WrappedMetadata storage = new WrappedMetadata(metadata);
                            wrapper.set(Type.UNSIGNED_BYTE, 0, (short)entityData.replacementId());
                            if (entityData.hasBaseMeta()) {
                                entityData.defaultMeta().createMeta(storage);
                            }
                        }
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_9_3.SPAWN_PAINTING, Entity1_10Types.EntityType.PAINTING);
        this.registerJoinGame(ClientboundPackets1_9_3.JOIN_GAME, Entity1_10Types.EntityType.PLAYER);
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
            }
        });
        this.registerRemoveEntities(ClientboundPackets1_9_3.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_9_3.ENTITY_METADATA, Types1_9.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        this.mapEntityTypeWithData(Entity1_10Types.EntityType.POLAR_BEAR, Entity1_10Types.EntityType.SHEEP).plainName();
        final boolean b;
        this.filter().type(Entity1_10Types.EntityType.POLAR_BEAR).index(13).handler((event, meta) -> {
            b = (boolean)meta.getValue();
            meta.setTypeAndValue(MetaType1_9.Byte, (byte)(b ? 14 : 0));
            return;
        });
        this.filter().type(Entity1_10Types.EntityType.ZOMBIE).index(13).handler((event, meta) -> {
            if ((int)meta.getValue() == 6) {
                meta.setValue(0);
            }
            return;
        });
        this.filter().type(Entity1_10Types.EntityType.SKELETON).index(12).handler((event, meta) -> {
            if ((int)meta.getValue() == 2) {
                meta.setValue(0);
            }
            return;
        });
        this.filter().removeIndex(5);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_10Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return Entity1_10Types.getTypeFromId(typeId, true);
    }
}
