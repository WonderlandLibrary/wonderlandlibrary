// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.AbstractCollection;

public abstract class AbstractIntCollection extends AbstractCollection<Integer> implements IntCollection
{
    protected AbstractIntCollection() {
    }
    
    @Override
    public abstract IntIterator iterator();
    
    @Override
    public boolean add(final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(final int k) {
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final int k) {
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    @Override
    public boolean add(final Integer key) {
        return super.add(key);
    }
    
    @Deprecated
    @Override
    public boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    @Override
    public boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Override
    public int[] toArray(int[] a) {
        final int size = this.size();
        if (a == null) {
            a = new int[size];
        }
        else if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        IntIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public int[] toIntArray() {
        return this.toArray((int[])null);
    }
    
    @Deprecated
    @Override
    public int[] toIntArray(final int[] a) {
        return this.toArray(a);
    }
    
    @Override
    public final void forEach(final IntConsumer action) {
        super.forEach(action);
    }
    
    @Override
    public final boolean removeIf(final IntPredicate filter) {
        return super.removeIf(filter);
    }
    
    @Override
    public boolean addAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        if (c instanceof IntCollection) {
            return this.addAll((IntCollection)c);
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean containsAll(final IntCollection c) {
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextInt())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        if (c instanceof IntCollection) {
            return this.containsAll((IntCollection)c);
        }
        return super.containsAll(c);
    }
    
    @Override
    public boolean removeAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        if (c instanceof IntCollection) {
            return this.removeAll((IntCollection)c);
        }
        return super.removeAll(c);
    }
    
    @Override
    public boolean retainAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextInt())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        if (c instanceof IntCollection) {
            return this.retainAll((IntCollection)c);
        }
        return super.retainAll(c);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final IntIterator i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final int k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
