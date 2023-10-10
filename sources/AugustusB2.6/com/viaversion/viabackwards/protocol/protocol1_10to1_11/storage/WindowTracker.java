// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage;

import com.viaversion.viaversion.api.connection.StorableObject;

public class WindowTracker implements StorableObject
{
    private String inventory;
    private int entityId;
    
    public WindowTracker() {
        this.entityId = -1;
    }
    
    public String getInventory() {
        return this.inventory;
    }
    
    public void setInventory(final String inventory) {
        this.inventory = inventory;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final int entityId) {
        this.entityId = entityId;
    }
    
    @Override
    public String toString() {
        return "WindowTracker{inventory='" + this.inventory + '\'' + ", entityId=" + this.entityId + '}';
    }
}
