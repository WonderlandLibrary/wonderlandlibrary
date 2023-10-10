// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

public interface TriConsumer<K, V, S>
{
    void accept(final K k, final V v, final S s);
}
