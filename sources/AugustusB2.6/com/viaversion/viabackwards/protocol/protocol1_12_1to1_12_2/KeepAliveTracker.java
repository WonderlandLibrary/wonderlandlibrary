// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_1to1_12_2;

import com.viaversion.viaversion.api.connection.StorableObject;

public class KeepAliveTracker implements StorableObject
{
    private long keepAlive;
    
    public KeepAliveTracker() {
        this.keepAlive = 2147483647L;
    }
    
    public long getKeepAlive() {
        return this.keepAlive;
    }
    
    public void setKeepAlive(final long keepAlive) {
        this.keepAlive = keepAlive;
    }
    
    @Override
    public String toString() {
        return "KeepAliveTracker{keepAlive=" + this.keepAlive + '}';
    }
}
