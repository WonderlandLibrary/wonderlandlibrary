// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Collection;

public interface BigList<K> extends Collection<K>, Size64
{
    K get(final long p0);
    
    K remove(final long p0);
    
    K set(final long p0, final K p1);
    
    void add(final long p0, final K p1);
    
    void size(final long p0);
    
    boolean addAll(final long p0, final Collection<? extends K> p1);
    
    long indexOf(final Object p0);
    
    long lastIndexOf(final Object p0);
    
    BigListIterator<K> listIterator();
    
    BigListIterator<K> listIterator(final long p0);
    
    BigList<K> subList(final long p0, final long p1);
    
    @Deprecated
    default int size() {
        return super.size();
    }
}
