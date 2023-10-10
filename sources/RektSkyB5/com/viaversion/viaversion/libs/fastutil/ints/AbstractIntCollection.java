/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntPredicate;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractIntCollection
extends AbstractCollection<Integer>
implements IntCollection {
    protected AbstractIntCollection() {
    }

    @Override
    public abstract IntIterator iterator();

    @Override
    public boolean add(int k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(int k2) {
        IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k2 != iterator.nextInt()) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean rem(int k2) {
        IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k2 != iterator.nextInt()) continue;
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
    public int[] toArray(int[] a2) {
        int size = this.size();
        if (a2 == null) {
            a2 = new int[size];
        } else if (a2.length < size) {
            a2 = Arrays.copyOf(a2, size);
        }
        IntIterators.unwrap(this.iterator(), a2);
        return a2;
    }

    @Override
    public int[] toIntArray() {
        return this.toArray((int[])null);
    }

    @Override
    @Deprecated
    public int[] toIntArray(int[] a2) {
        return this.toArray(a2);
    }

    @Override
    public final void forEach(IntConsumer action) {
        IntCollection.super.forEach(action);
    }

    @Override
    public final boolean removeIf(IntPredicate filter) {
        return IntCollection.super.removeIf(filter);
    }

    @Override
    public boolean addAll(IntCollection c2) {
        boolean retVal = false;
        IntIterator i2 = c2.iterator();
        while (i2.hasNext()) {
            if (!this.add(i2.nextInt())) continue;
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c2) {
        if (c2 instanceof IntCollection) {
            return this.addAll((IntCollection)c2);
        }
        return super.addAll(c2);
    }

    @Override
    public boolean containsAll(IntCollection c2) {
        IntIterator i2 = c2.iterator();
        while (i2.hasNext()) {
            if (this.contains(i2.nextInt())) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c2) {
        if (c2 instanceof IntCollection) {
            return this.containsAll((IntCollection)c2);
        }
        return super.containsAll(c2);
    }

    @Override
    public boolean removeAll(IntCollection c2) {
        boolean retVal = false;
        IntIterator i2 = c2.iterator();
        while (i2.hasNext()) {
            if (!this.rem(i2.nextInt())) continue;
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        if (c2 instanceof IntCollection) {
            return this.removeAll((IntCollection)c2);
        }
        return super.removeAll(c2);
    }

    @Override
    public boolean retainAll(IntCollection c2) {
        boolean retVal = false;
        IntIterator i2 = this.iterator();
        while (i2.hasNext()) {
            if (c2.contains(i2.nextInt())) continue;
            i2.remove();
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        if (c2 instanceof IntCollection) {
            return this.retainAll((IntCollection)c2);
        }
        return super.retainAll(c2);
    }

    @Override
    public String toString() {
        StringBuilder s2 = new StringBuilder();
        IntIterator i2 = this.iterator();
        int n2 = this.size();
        boolean first = true;
        s2.append("{");
        while (n2-- != 0) {
            if (first) {
                first = false;
            } else {
                s2.append(", ");
            }
            int k2 = i2.nextInt();
            s2.append(String.valueOf(k2));
        }
        s2.append("}");
        return s2.toString();
    }
}

