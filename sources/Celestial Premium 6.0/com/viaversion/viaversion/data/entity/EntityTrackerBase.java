/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.data.entity;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.StoredEntityImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EntityTrackerBase
implements EntityTracker,
ClientEntityIdChangeListener {
    private final Map<Integer, EntityType> entityTypes = new ConcurrentHashMap<Integer, EntityType>();
    private final Map<Integer, StoredEntityData> entityData;
    private final UserConnection connection;
    private final EntityType playerType;
    private int clientEntityId = -1;
    private int currentWorldSectionHeight = 16;
    private int currentMinY;

    public EntityTrackerBase(UserConnection connection, @Nullable EntityType playerType) {
        this(connection, playerType, false);
    }

    public EntityTrackerBase(UserConnection connection, @Nullable EntityType playerType, boolean storesEntityData) {
        this.connection = connection;
        this.playerType = playerType;
        this.entityData = storesEntityData ? new ConcurrentHashMap() : null;
    }

    @Override
    public UserConnection user() {
        return this.connection;
    }

    @Override
    public void addEntity(int id, EntityType type) {
        this.entityTypes.put(id, type);
    }

    @Override
    public boolean hasEntity(int id) {
        return this.entityTypes.containsKey(id);
    }

    @Override
    public @Nullable EntityType entityType(int id) {
        return this.entityTypes.get(id);
    }

    @Override
    public @Nullable StoredEntityData entityData(int id) {
        EntityType type = this.entityType(id);
        return type != null ? this.entityData.computeIfAbsent(id, s -> new StoredEntityImpl(type)) : null;
    }

    @Override
    public @Nullable StoredEntityData entityDataIfPresent(int id) {
        return this.entityData.get(id);
    }

    @Override
    public void removeEntity(int id) {
        this.entityTypes.remove(id);
        if (this.entityData != null) {
            this.entityData.remove(id);
        }
    }

    @Override
    public int clientEntityId() {
        return this.clientEntityId;
    }

    @Override
    public void setClientEntityId(int clientEntityId) {
        StoredEntityData data;
        Preconditions.checkNotNull(this.playerType);
        this.entityTypes.put(clientEntityId, this.playerType);
        if (this.clientEntityId != -1 && this.entityData != null && (data = this.entityData.remove(this.clientEntityId)) != null) {
            this.entityData.put(clientEntityId, data);
        }
        this.clientEntityId = clientEntityId;
    }

    @Override
    public int currentWorldSectionHeight() {
        return this.currentWorldSectionHeight;
    }

    @Override
    public void setCurrentWorldSectionHeight(int currentWorldSectionHeight) {
        this.currentWorldSectionHeight = currentWorldSectionHeight;
    }

    @Override
    public int currentMinY() {
        return this.currentMinY;
    }

    @Override
    public void setCurrentMinY(int currentMinY) {
        this.currentMinY = currentMinY;
    }
}

