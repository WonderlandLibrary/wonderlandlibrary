// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.tag.limiter;

public interface TagLimiter
{
    default TagLimiter create(final int maxBytes, final int maxLevels) {
        return new TagLimiterImpl(maxBytes, maxLevels);
    }
    
    default TagLimiter noop() {
        return NoopTagLimiter.INSTANCE;
    }
    
    void countBytes(final int p0);
    
    void checkLevel(final int p0);
    
    default void countByte() {
        this.countBytes(1);
    }
    
    default void countShort() {
        this.countBytes(2);
    }
    
    default void countInt() {
        this.countBytes(4);
    }
    
    default void countFloat() {
        this.countBytes(4);
    }
    
    default void countLong() {
        this.countBytes(8);
    }
    
    default void countDouble() {
        this.countBytes(8);
    }
    
    int maxBytes();
    
    int maxLevels();
    
    int bytes();
}
