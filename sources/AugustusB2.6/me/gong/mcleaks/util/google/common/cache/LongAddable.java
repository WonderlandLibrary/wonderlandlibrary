// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.cache;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
interface LongAddable
{
    void increment();
    
    void add(final long p0);
    
    long sum();
}
