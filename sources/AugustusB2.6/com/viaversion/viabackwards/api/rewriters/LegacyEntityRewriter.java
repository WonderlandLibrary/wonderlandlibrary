// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viabackwards.ViaBackwards;
import java.util.function.Function;
import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.api.entities.storage.EntityObjectData;
import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viaversion.api.minecraft.entities.ObjectType;
import java.util.Map;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public abstract class LegacyEntityRewriter<T extends BackwardsProtocol> extends EntityRewriterBase<T>
{
    private final Map<ObjectType, EntityData> objectTypes;
    
    protected LegacyEntityRewriter(final T protocol) {
        this(protocol, MetaType1_9.String, MetaType1_9.Boolean);
    }
    
    protected LegacyEntityRewriter(final T protocol, final MetaType displayType, final MetaType displayVisibilityType) {
        super(protocol, displayType, 2, displayVisibilityType, 3);
        this.objectTypes = new HashMap<ObjectType, EntityData>();
    }
    
    protected EntityObjectData mapObjectType(final ObjectType oldObjectType, final ObjectType replacement, final int data) {
        final EntityObjectData entData = new EntityObjectData(this.protocol, oldObjectType.getType().name(), oldObjectType.getId(), replacement.getId(), data);
        this.objectTypes.put(oldObjectType, entData);
        return entData;
    }
    
    protected EntityData getObjectData(final ObjectType type) {
        return this.objectTypes.get(type);
    }
    
    protected void registerRespawn(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                final ClientWorld clientWorld;
                this.handler(wrapper -> {
                    clientWorld = wrapper.user().get(ClientWorld.class);
                    clientWorld.setEnvironment(wrapper.get((Type<Integer>)Type.INT, 0));
                });
            }
        });
    }
    
    protected void registerJoinGame(final ClientboundPacketType packetType, final EntityType playerType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                final EntityType val$playerType;
                final ClientWorld clientChunks;
                this.handler(wrapper -> {
                    val$playerType = playerType;
                    clientChunks = wrapper.user().get(ClientWorld.class);
                    clientChunks.setEnvironment(wrapper.get((Type<Integer>)Type.INT, 1));
                    LegacyEntityRewriter.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.INT, 0), val$playerType);
                });
            }
        });
    }
    
    @Override
    public void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> oldMetaType, final Type<List<Metadata>> newMetaType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                if (oldMetaType != null) {
                    this.map(oldMetaType, newMetaType);
                }
                else {
                    this.map(newMetaType);
                }
                final List<Metadata> metadata;
                this.handler(wrapper -> {
                    metadata = wrapper.get(newMetaType, 0);
                    LegacyEntityRewriter.this.handleMetadata(wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadata, wrapper.user());
                });
            }
        });
    }
    
    @Override
    public void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> metaType) {
        this.registerMetadataRewriter(packetType, null, metaType);
    }
    
    protected PacketHandler getMobSpawnRewriter(final Type<List<Metadata>> metaType) {
        final int entityId;
        final EntityType type;
        final List<Metadata> metadata;
        final EntityData entityData;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            type = this.tracker(wrapper.user()).entityType(entityId);
            metadata = wrapper.get(metaType, 0);
            this.handleMetadata(entityId, metadata, wrapper.user());
            entityData = this.entityDataForType(type);
            if (entityData != null) {
                wrapper.set(Type.VAR_INT, 1, entityData.replacementId());
                if (entityData.hasBaseMeta()) {
                    entityData.defaultMeta().createMeta(new WrappedMetadata(metadata));
                }
            }
        };
    }
    
    protected PacketHandler getObjectTrackerHandler() {
        return wrapper -> this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), this.getObjectTypeFromId(wrapper.get((Type<Byte>)Type.BYTE, 0)));
    }
    
    protected PacketHandler getTrackerAndMetaHandler(final Type<List<Metadata>> metaType, final EntityType entityType) {
        final List<Metadata> metadata;
        return wrapper -> {
            this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), entityType);
            metadata = wrapper.get(metaType, 0);
            this.handleMetadata(wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadata, wrapper.user());
        };
    }
    
    protected PacketHandler getObjectRewriter(final Function<Byte, ObjectType> objectGetter) {
        final ObjectType type;
        EntityData data;
        return wrapper -> {
            type = objectGetter.apply(wrapper.get((Type<Byte>)Type.BYTE, 0));
            if (type == null) {
                ViaBackwards.getPlatform().getLogger().warning("Could not find Entity Type" + wrapper.get((Type<Object>)Type.BYTE, 0));
            }
            else {
                data = this.getObjectData(type);
                if (data != null) {
                    wrapper.set(Type.BYTE, 0, (byte)data.replacementId());
                    if (data.objectData() != -1) {
                        wrapper.set(Type.INT, 0, data.objectData());
                    }
                }
            }
        };
    }
    
    protected EntityType getObjectTypeFromId(final int typeId) {
        return this.typeFromId(typeId);
    }
    
    @Deprecated
    protected void addTrackedEntity(final PacketWrapper wrapper, final int entityId, final EntityType type) throws Exception {
        this.tracker(wrapper.user()).addEntity(entityId, type);
    }
}
