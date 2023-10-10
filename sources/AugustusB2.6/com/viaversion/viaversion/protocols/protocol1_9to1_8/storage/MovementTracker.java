// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import com.viaversion.viaversion.api.connection.StorableObject;

public class MovementTracker implements StorableObject
{
    private static final long IDLE_PACKET_DELAY = 50L;
    private static final long IDLE_PACKET_LIMIT = 20L;
    private long nextIdlePacket;
    private boolean ground;
    
    public MovementTracker() {
        this.nextIdlePacket = 0L;
        this.ground = true;
    }
    
    public void incrementIdlePacket() {
        this.nextIdlePacket = Math.max(this.nextIdlePacket + 50L, System.currentTimeMillis() - 1000L);
    }
    
    public long getNextIdlePacket() {
        return this.nextIdlePacket;
    }
    
    public boolean isGround() {
        return this.ground;
    }
    
    public void setGround(final boolean ground) {
        this.ground = ground;
    }
}
