// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.connection;

public abstract class StoredObject implements StorableObject
{
    private final UserConnection user;
    
    protected StoredObject(final UserConnection user) {
        this.user = user;
    }
    
    public UserConnection getUser() {
        return this.user;
    }
}
