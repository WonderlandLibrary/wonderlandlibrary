// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.data.entity;

import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.connection.UserConnection;

public interface EntityTracker
{
    UserConnection user();
    
    void addEntity(final int p0, final EntityType p1);
    
    boolean hasEntity(final int p0);
    
    EntityType entityType(final int p0);
    
    void removeEntity(final int p0);
    
    void clearEntities();
    
    StoredEntityData entityData(final int p0);
    
    StoredEntityData entityDataIfPresent(final int p0);
    
    int clientEntityId();
    
    void setClientEntityId(final int p0);
    
    int currentWorldSectionHeight();
    
    void setCurrentWorldSectionHeight(final int p0);
    
    int currentMinY();
    
    void setCurrentMinY(final int p0);
    
    String currentWorld();
    
    void setCurrentWorld(final String p0);
    
    int biomesSent();
    
    void setBiomesSent(final int p0);
}
