// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_14_4to1_15.data;

import com.viaversion.viaversion.api.connection.StorableObject;

public class ImmediateRespawn implements StorableObject
{
    private boolean immediateRespawn;
    
    public boolean isImmediateRespawn() {
        return this.immediateRespawn;
    }
    
    public void setImmediateRespawn(final boolean immediateRespawn) {
        this.immediateRespawn = immediateRespawn;
    }
}
