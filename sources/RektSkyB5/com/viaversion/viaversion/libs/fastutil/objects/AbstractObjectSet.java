/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractObjectSet<K>
extends AbstractObjectCollection<K>
implements Cloneable,
ObjectSet<K> {
    protected AbstractObjectSet() {
    }

    @Override
    public abstract ObjectIterator<K> iterator();

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
        return this.containsAll(s2);
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int n2 = this.size();
        Iterator i2 = this.iterator();
        while (n2-- != 0) {
            Object k2 = i2.next();
            h2 += k2 == null ? 0 : k2.hashCode();
        }
        return h2;
    }
}

