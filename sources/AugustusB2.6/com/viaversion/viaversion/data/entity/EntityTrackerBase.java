// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.data.entity;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.flare.fastutil.Int2ObjectSyncMap;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.data.entity.EntityTracker;

public class EntityTrackerBase implements EntityTracker, ClientEntityIdChangeListener
{
    private final Int2ObjectMap<EntityType> entityTypes;
    private final Int2ObjectMap<StoredEntityData> entityData;
    private final UserConnection connection;
    private final EntityType playerType;
    private int clientEntityId;
    private int currentWorldSectionHeight;
    private int currentMinY;
    private String currentWorld;
    private int biomesSent;
    
    public EntityTrackerBase(final UserConnection connection, final EntityType playerType) {
        this(connection, playerType, false);
    }
    
    public EntityTrackerBase(final UserConnection connection, final EntityType playerType, final boolean storesEntityData) {
        this.entityTypes = (Int2ObjectMap<EntityType>)Int2ObjectSyncMap.hashmap();
        this.clientEntityId = -1;
        this.currentWorldSectionHeight = 16;
        this.biomesSent = -1;
        this.connection = connection;
        this.playerType = playerType;
        this.entityData = (Int2ObjectMap<StoredEntityData>)(storesEntityData ? Int2ObjectSyncMap.hashmap() : null);
    }
    
    @Override
    public UserConnection user() {
        return this.connection;
    }
    
    @Override
    public void addEntity(final int id, final EntityType type) {
        this.entityTypes.put(id, type);
    }
    
    @Override
    public boolean hasEntity(final int id) {
        return this.entityTypes.containsKey(id);
    }
    
    @Override
    public EntityType entityType(final int id) {
        return this.entityTypes.get(id);
    }
    
    @Override
    public StoredEntityData entityData(final int id) {
        Preconditions.checkArgument(this.entityData != null, (Object)"Entity data storage has to be explicitly enabled via the constructor");
        final EntityType type = this.entityType(id);
        return (type != null) ? this.entityData.computeIfAbsent(id, s -> new StoredEntityImpl(type)) : null;
    }
    
    @Override
    public StoredEntityData entityDataIfPresent(final int id) {
        Preconditions.checkArgument(this.entityData != null, (Object)"Entity data storage has to be explicitly enabled via the constructor");
        return this.entityData.get(id);
    }
    
    @Override
    public void removeEntity(final int id) {
        this.entityTypes.remove(id);
        if (this.entityData != null) {
            this.entityData.remove(id);
        }
    }
    
    @Override
    public void clearEntities() {
        this.entityTypes.clear();
        if (this.entityData != null) {
            this.entityData.clear();
        }
    }
    
    @Override
    public int clientEntityId() {
        return this.clientEntityId;
    }
    
    @Override
    public void setClientEntityId(final int clientEntityId) {
        Preconditions.checkNotNull(this.playerType);
        this.entityTypes.put(clientEntityId, this.playerType);
        if (this.clientEntityId != -1 && this.entityData != null) {
            final StoredEntityData data = this.entityData.remove(this.clientEntityId);
            if (data != null) {
                this.entityData.put(clientEntityId, data);
            }
        }
        this.clientEntityId = clientEntityId;
    }
    
    @Override
    public int currentWorldSectionHeight() {
        return this.currentWorldSectionHeight;
    }
    
    @Override
    public void setCurrentWorldSectionHeight(final int currentWorldSectionHeight) {
        this.currentWorldSectionHeight = currentWorldSectionHeight;
    }
    
    @Override
    public int currentMinY() {
        return this.currentMinY;
    }
    
    @Override
    public void setCurrentMinY(final int currentMinY) {
        this.currentMinY = currentMinY;
    }
    
    @Override
    public String currentWorld() {
        return this.currentWorld;
    }
    
    @Override
    public void setCurrentWorld(final String currentWorld) {
        this.currentWorld = currentWorld;
    }
    
    @Override
    public int biomesSent() {
        return this.biomesSent;
    }
    
    @Override
    public void setBiomesSent(final int biomesSent) {
        this.biomesSent = biomesSent;
    }
}
