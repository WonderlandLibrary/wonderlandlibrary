// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.connection.StoredObject;

public class ClientWorld extends StoredObject
{
    private Environment environment;
    
    public ClientWorld(final UserConnection connection) {
        super(connection);
    }
    
    public Environment getEnvironment() {
        return this.environment;
    }
    
    public void setEnvironment(final int environmentId) {
        this.environment = Environment.getEnvironmentById(environmentId);
    }
}
