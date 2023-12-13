// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;

public abstract class AbstractIntSortedSet extends AbstractIntSet implements IntSortedSet
{
    protected AbstractIntSortedSet() {
    }
    
    @Override
    public abstract IntBidirectionalIterator iterator();
}
