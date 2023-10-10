// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.storage;

public final class WolfDataMaskStorage
{
    private byte tameableMask;
    
    public WolfDataMaskStorage(final byte tameableMask) {
        this.tameableMask = tameableMask;
    }
    
    public void setTameableMask(final byte tameableMask) {
        this.tameableMask = tameableMask;
    }
    
    public byte tameableMask() {
        return this.tameableMask;
    }
}
