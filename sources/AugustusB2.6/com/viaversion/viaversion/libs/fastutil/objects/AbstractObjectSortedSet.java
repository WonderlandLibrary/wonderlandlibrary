// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;

public abstract class AbstractObjectSortedSet<K> extends AbstractObjectSet<K> implements ObjectSortedSet<K>
{
    protected AbstractObjectSortedSet() {
    }
    
    @Override
    public abstract ObjectBidirectionalIterator<K> iterator();
}
