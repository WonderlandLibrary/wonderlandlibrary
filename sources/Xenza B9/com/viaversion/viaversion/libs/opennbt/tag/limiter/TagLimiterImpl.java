// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.tag.limiter;

final class TagLimiterImpl implements TagLimiter
{
    private final int maxBytes;
    private final int maxLevels;
    private int bytes;
    
    TagLimiterImpl(final int maxBytes, final int maxLevels) {
        this.maxBytes = maxBytes;
        this.maxLevels = maxLevels;
    }
    
    @Override
    public void countBytes(final int bytes) {
        this.bytes += bytes;
        if (this.bytes >= this.maxBytes) {
            throw new IllegalArgumentException("NBT data larger than expected (capped at " + this.maxBytes + ")");
        }
    }
    
    @Override
    public void checkLevel(final int nestedLevel) {
        if (nestedLevel >= this.maxLevels) {
            throw new IllegalArgumentException("Nesting level higher than expected (capped at " + this.maxLevels + ")");
        }
    }
    
    @Override
    public int maxBytes() {
        return this.maxBytes;
    }
    
    @Override
    public int maxLevels() {
        return this.maxLevels;
    }
    
    @Override
    public int bytes() {
        return this.bytes;
    }
}
