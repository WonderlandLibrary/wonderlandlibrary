// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractIntSet extends AbstractIntCollection implements Cloneable, IntSet
{
    protected AbstractIntSet() {
    }
    
    @Override
    public abstract IntIterator iterator();
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        final Set<?> s = (Set<?>)o;
        if (s.size() != this.size()) {
            return false;
        }
        if (s instanceof IntSet) {
            return this.containsAll((IntCollection)s);
        }
        return this.containsAll(s);
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final IntIterator i = this.iterator();
        while (n-- != 0) {
            final int k = i.nextInt();
            h += k;
        }
        return h;
    }
    
    @Override
    public boolean remove(final int k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final int k) {
        return this.remove(k);
    }
}
