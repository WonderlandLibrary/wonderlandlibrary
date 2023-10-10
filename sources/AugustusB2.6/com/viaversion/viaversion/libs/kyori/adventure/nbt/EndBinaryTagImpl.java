// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

final class EndBinaryTagImpl extends AbstractBinaryTag implements EndBinaryTag
{
    static final EndBinaryTagImpl INSTANCE;
    
    @Override
    public boolean equals(final Object that) {
        return this == that;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    static {
        INSTANCE = new EndBinaryTagImpl();
    }
}
