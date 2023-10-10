// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.cache;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Weigher<K, V>
{
    int weigh(final K p0, final V p1);
}
