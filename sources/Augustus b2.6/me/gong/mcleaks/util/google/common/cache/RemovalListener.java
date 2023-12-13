// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.cache;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface RemovalListener<K, V>
{
    void onRemoval(final RemovalNotification<K, V> p0);
}
