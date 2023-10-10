// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

@ElementTypesAreNonnullByDefault
interface LongAddable
{
    void increment();
    
    void add(final long p0);
    
    long sum();
}
