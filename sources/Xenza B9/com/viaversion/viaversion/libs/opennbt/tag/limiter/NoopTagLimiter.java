// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.tag.limiter;

final class NoopTagLimiter implements TagLimiter
{
    static final TagLimiter INSTANCE;
    
    @Override
    public void countBytes(final int bytes) {
    }
    
    @Override
    public void checkLevel(final int nestedLevel) {
    }
    
    @Override
    public int maxBytes() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public int maxLevels() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public int bytes() {
        return 0;
    }
    
    static {
        INSTANCE = new NoopTagLimiter();
    }
}
