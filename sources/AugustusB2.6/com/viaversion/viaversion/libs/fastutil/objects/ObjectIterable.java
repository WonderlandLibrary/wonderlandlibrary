// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;
import java.util.Spliterator;

public interface ObjectIterable<K> extends Iterable<K>
{
    ObjectIterator<K> iterator();
    
    default ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
    }
}
