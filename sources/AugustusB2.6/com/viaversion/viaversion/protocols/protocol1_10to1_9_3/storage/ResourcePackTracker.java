// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_10to1_9_3.storage;

import com.viaversion.viaversion.api.connection.StorableObject;

public class ResourcePackTracker implements StorableObject
{
    private String lastHash;
    
    public ResourcePackTracker() {
        this.lastHash = "";
    }
    
    public String getLastHash() {
        return this.lastHash;
    }
    
    public void setLastHash(final String lastHash) {
        this.lastHash = lastHash;
    }
    
    @Override
    public String toString() {
        return "ResourcePackTracker{lastHash='" + this.lastHash + '\'' + '}';
    }
}
