/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import java.util.Set;

public abstract class AbstractIntSet
extends AbstractIntCollection
implements Cloneable,
IntSet {
    protected AbstractIntSet() {
    }

    @Override
    public abstract IntIterator iterator();

    @Override
    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Set)) {
            return false;
        }
        Set s2 = (Set)o2;
        if (s2.size() != this.size()) {
            return false;
        }
        if (s2 instanceof IntSet) {
            return this.containsAll((IntSet)s2);
        }
        return this.containsAll(s2);
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int n2 = this.size();
        IntIterator i2 = this.iterator();
        while (n2-- != 0) {
            int k2 = i2.nextInt();
            h2 += k2;
        }
        return h2;
    }

    @Override
    public boolean remove(int k2) {
        return super.rem(k2);
    }

    @Override
    @Deprecated
    public boolean rem(int k2) {
        return this.remove(k2);
    }
}

