// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public abstract class EntityRewriterBase<T extends BackwardsProtocol> extends EntityRewriter<T>
{
    private final Int2ObjectMap<EntityData> entityDataMappings;
    private final MetaType displayNameMetaType;
    private final MetaType displayVisibilityMetaType;
    private final int displayNameIndex;
    private final int displayVisibilityIndex;
    
    EntityRewriterBase(final T protocol, final MetaType displayNameMetaType, final int displayNameIndex, final MetaType displayVisibilityMetaType, final int displayVisibilityIndex) {
        super(protocol, false);
        this.entityDataMappings = new Int2ObjectOpenHashMap<EntityData>();
        this.displayNameMetaType = displayNameMetaType;
        this.displayNameIndex = displayNameIndex;
        this.displayVisibilityMetaType = displayVisibilityMetaType;
        this.displayVisibilityIndex = displayVisibilityIndex;
    }
    
    @Override
    public void handleMetadata(final int entityId, final List<Metadata> metadataList, final UserConnection connection) {
        super.handleMetadata(entityId, metadataList, connection);
        final EntityType type = this.tracker(connection).entityType(entityId);
        if (type == null) {
            return;
        }
        final EntityData entityData = this.entityDataForType(type);
        final Metadata meta = this.getMeta(this.displayNameIndex, metadataList);
        if (meta != null && entityData != null && entityData.mobName() != null && (meta.getValue() == null || meta.getValue().toString().isEmpty()) && meta.metaType().typeId() == this.displayNameMetaType.typeId()) {
            meta.setValue(entityData.mobName());
            if (ViaBackwards.getConfig().alwaysShowOriginalMobName()) {
                this.removeMeta(this.displayVisibilityIndex, metadataList);
                metadataList.add(new Metadata(this.displayVisibilityIndex, this.displayVisibilityMetaType, true));
            }
        }
        if (entityData != null && entityData.hasBaseMeta()) {
            entityData.defaultMeta().createMeta(new WrappedMetadata(metadataList));
        }
    }
    
    protected Metadata getMeta(final int metaIndex, final List<Metadata> metadataList) {
        for (final Metadata metadata : metadataList) {
            if (metadata.id() == metaIndex) {
                return metadata;
            }
        }
        return null;
    }
    
    protected void removeMeta(final int metaIndex, final List<Metadata> metadataList) {
        metadataList.removeIf(meta -> meta.id() == metaIndex);
    }
    
    protected boolean hasData(final EntityType type) {
        return this.entityDataMappings.containsKey(type.getId());
    }
    
    protected EntityData entityDataForType(final EntityType type) {
        return this.entityDataMappings.get(type.getId());
    }
    
    protected StoredEntityData storedEntityData(final MetaHandlerEvent event) {
        return this.tracker(event.user()).entityData(event.entityId());
    }
    
    protected EntityData mapEntityTypeWithData(final EntityType type, final EntityType mappedType) {
        Preconditions.checkArgument(type.getClass() == mappedType.getClass());
        final int mappedReplacementId = this.newEntityId(mappedType.getId());
        final EntityData data = new EntityData(this.protocol, type, mappedReplacementId);
        this.mapEntityType(type.getId(), mappedReplacementId);
        this.entityDataMappings.put(type.getId(), data);
        return data;
    }
    
    @Override
    public <E extends Enum> void mapTypes(final EntityType[] oldTypes, final Class<E> newTypeClass) {
        if (this.typeMappings == null) {
            (this.typeMappings = new Int2IntOpenHashMap(oldTypes.length, 0.99f)).defaultReturnValue(-1);
        }
        for (final EntityType oldType : oldTypes) {
            try {
                final E newType = java.lang.Enum.valueOf(newTypeClass, oldType.name());
                this.typeMappings.put(oldType.getId(), ((EntityType)newType).getId());
            }
            catch (IllegalArgumentException ex) {}
        }
    }
    
    public void registerMetaTypeHandler(final MetaType itemType, final MetaType blockType, final MetaType particleType, final MetaType optChatType) {
        final MetaType type;
        int data;
        JsonElement text;
        this.filter().handler((event, meta) -> {
            type = meta.metaType();
            if (itemType != null && type == itemType) {
                this.protocol.getItemRewriter().handleItemToClient((Item)meta.value());
            }
            else if (blockType != null && type == blockType) {
                data = (int)meta.value();
                meta.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
            }
            else if (particleType != null && type == particleType) {
                this.rewriteParticle((Particle)meta.value());
            }
            else if (optChatType != null && type == optChatType) {
                text = (JsonElement)meta.value();
                if (text != null) {
                    this.protocol.getTranslatableRewriter().processText(text);
                }
            }
        });
    }
    
    protected PacketHandler getTrackerHandler(final Type<? extends Number> intType, final int typeIndex) {
        final Number id;
        return wrapper -> {
            id = wrapper.get(intType, typeIndex);
            this.tracker(wrapper.user()).addEntity(wrapper.get((Type<Integer>)Type.VAR_INT, 0), this.typeFromId(id.intValue()));
        };
    }
    
    protected PacketHandler getTrackerHandler() {
        return this.getTrackerHandler(Type.VAR_INT, 1);
    }
    
    protected PacketHandler getTrackerHandler(final EntityType entityType, final Type<? extends Number> intType) {
        return wrapper -> this.tracker(wrapper.user()).addEntity((int)wrapper.get(intType, 0), entityType);
    }
    
    protected PacketHandler getDimensionHandler(final int index) {
        final ClientWorld clientWorld;
        final int dimensionId;
        return wrapper -> {
            clientWorld = wrapper.user().get(ClientWorld.class);
            dimensionId = wrapper.get((Type<Integer>)Type.INT, index);
            clientWorld.setEnvironment(dimensionId);
        };
    }
}
