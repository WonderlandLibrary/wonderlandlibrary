/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import java.util.AbstractCollection;

public abstract class AbstractIntCollection
extends AbstractCollection<Integer>
implements IntCollection {
    protected AbstractIntCollection() {
    }

    @Override
    public abstract IntIterator iterator();

    @Override
    public boolean add(int k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(int k) {
        IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k != iterator.nextInt()) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean rem(int k) {
        IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k != iterator.nextInt()) continue;
            iterator.remove();
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean add(Integer key) {
        return IntCollection.super.add(key);
    }

    @Override
    @Deprecated
    public boolean contains(Object key) {
        return IntCollection.super.contains(key);
    }

    @Override
    @Deprecated
    public boolean remove(Object key) {
        return IntCollection.super.remove(key);
    }

    @Override
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size()) {
            a = new int[this.size()];
        }
        IntIterators.unwrap(this.iterator(), a);
        return a;
    }

    @Override
    public int[] toIntArray() {
        return this.toArray((int[])null);
    }

    @Override
    @Deprecated
    public int[] toIntArray(int[] a) {
        return this.toArray(a);
    }

    @Override
    public boolean addAll(IntCollection c) {
        boolean retVal = false;
        IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.add(i.nextInt())) continue;
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean containsAll(IntCollection c) {
        IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.contains(i.nextInt())) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(IntCollection c) {
        boolean retVal = false;
        IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.rem(i.nextInt())) continue;
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean retainAll(IntCollection c) {
        boolean retVal = false;
        IntIterator i = this.iterator();
        while (i.hasNext()) {
            if (c.contains(i.nextInt())) continue;
            i.remove();
            retVal = true;
        }
        return retVal;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        IntIterator i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            int k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}

