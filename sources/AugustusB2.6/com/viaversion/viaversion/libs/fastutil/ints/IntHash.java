// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

public interface IntHash
{
    public interface Strategy
    {
        int hashCode(final int p0);
        
        boolean equals(final int p0, final int p1);
    }
}
